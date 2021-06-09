import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FileEvent, FileModel, FileObj } from '@app/_models';
import { BehaviorSubject } from 'rxjs';
import { webSocket } from 'rxjs/webSocket';


@Injectable()
export class DBService {
    fileSource: BehaviorSubject<FileModel[]> = new BehaviorSubject<FileModel[]>([]);
    private subject = webSocket('ws://localhost:8080/push');

    set files(data: any) {
        this.fileSource.next(data);
    }

    get files() {
        return this.fileSource.value.slice();
    }

    addTasks(file: FileModel) {
        const files = this.files;
        files.push();
        this.files = files;

    }

    public addTask(task: any) {
        const tasks = this.files;
        tasks.push(task);
        this.fileSource.next(tasks);
    }

    constructor(private httpClient: HttpClient) {

        this.subject.next({ message: 'message' });
        this.subject.subscribe((message: FileEvent) => {
            console.log(message);
            switch (message.action) {
                case 'add':
                    this.addTask(new FileObj(message.file));
                    break;

                default:
                    break;
            }
        });
    }
}
