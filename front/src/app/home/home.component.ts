import { DataSource } from '@angular/cdk/collections';
import { Component, Inject } from '@angular/core';
import { DBService } from '@app/_services';
import { FileExt, FileModel } from '@app/_models';
import { BehaviorSubject, Observable } from 'rxjs';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HttpClient, HttpEventType } from '@angular/common/http';



@Component({
    templateUrl: 'home.component.html',
    providers: [DBService],
})
export class HomeComponent {
    dataSource = new FileDataSource(this.dbService);
    displayedColumns: string[] = ['filename', 'created', 'user', 'action'];
    title = "hz";
    loading = false;
    constructor(public dbService: DBService, public dialog: MatDialog) {

    }

    openDialog() {
        const dialogRef = this.dialog.open(DialogUploadDialog, { width: '50%' });

        dialogRef.afterClosed().subscribe(result => {
            console.log(`Dialog result: ${result}`);
        });
    }
}

export class FileDataSource extends DataSource<FileModel> {
    data = new BehaviorSubject<FileModel[]>([]);
    constructor(private dbService: DBService) {
        super();
    }
    connect(): Observable<FileModel[]> {
        return this.dbService.fileSource;
    }

    disconnect() { }
}

@Component({
    selector: 'dialog-upload-dialog',
    templateUrl: 'dialog-upload-dialog.html',
})
export class DialogUploadDialog {
    filesExt: FileExt[] = [];
    isChecked = false;

    constructor(@Inject(MAT_DIALOG_DATA) public data: any, private http: HttpClient) {

    }

    onFileSelected(event) {
        const files = ([...event.target.files] || []).map(file => new FileExt(file));
        this.filesExt = this.filesExt.concat(files);
        files.forEach(fileExt =>
            this.upload(fileExt)
        )

    }
    upload(fileExt) {
        const formData = new FormData();
        formData.append("files", fileExt.file)
        this.http.post('/api/upload', formData, {
            reportProgress: true,
            observe: 'events'
        }).subscribe(resp => {
            if (resp.type === HttpEventType.Response) {
                console.log('Upload complete');
            }
            if (resp.type === HttpEventType.UploadProgress) {
                const percentDone = Math.round(100 * resp.loaded / resp.total);
                fileExt.percent = percentDone;
            }
        });
    }
}