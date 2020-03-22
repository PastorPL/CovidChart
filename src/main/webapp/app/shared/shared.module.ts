import { NgModule } from '@angular/core';
import { CovChartSharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { LoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { SpinnerComponent } from './spinner/spinner.component';

@NgModule({
  imports: [CovChartSharedLibsModule],
  declarations: [
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    SpinnerComponent
  ],
  entryComponents: [LoginModalComponent],
  exports: [
    CovChartSharedLibsModule,
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    SpinnerComponent
  ]
})
export class CovChartSharedModule {}
