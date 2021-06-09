export interface FileModel {
    id: number;
    filename: String;
    path: String;
    user_id: number;
    created: Date;
    modified: Date;
}
export class FileExt {
    load: boolean;
    percent: number;

    constructor(public file: File) {
        this.load = false;
        this.percent = 0;
    }
}

export class FileEvent {
    file: FileModel;
    action: string;
}

export class FileObj implements FileModel {
    id: number;
    filename: String;
    path: String;
    user_id: number;
    created: Date;
    modified: Date;
    ext: string;
    constructor(file: FileModel) {
        this.id = file.id;
        this.filename = file.filename;
        this.path = file.path;
        this.user_id = file.user_id;
        this.created = file.created;
        this.modified = file.modified;
        this.ext = file.filename.match(/\.[0-9a-z]+$/i)[0] || '';
    }

}