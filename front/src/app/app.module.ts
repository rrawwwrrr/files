import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { BasicAuthInterceptor, ErrorInterceptor } from './_helpers';
import { DialogUploadDialog, HomeComponent } from './home';
import { LoginComponent } from './login';;
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { MaterialModules } from './material';

@NgModule({
    imports: [
        BrowserModule,
        ReactiveFormsModule,
        HttpClientModule,
        AppRoutingModule,
        MaterialModules,
        BrowserAnimationsModule,
        FormsModule
    ],
    declarations: [
        AppComponent,
        HomeComponent,
        LoginComponent,
        DialogUploadDialog
    ],
    entryComponents: [HomeComponent, DialogUploadDialog],
    providers: [
        { provide: HTTP_INTERCEPTORS, useClass: BasicAuthInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }