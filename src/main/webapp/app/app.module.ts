import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { CovChartSharedModule } from 'app/shared/shared.module';
import { CovChartCoreModule } from 'app/core/core.module';
import { CovChartAppRoutingModule } from './app-routing.module';
import { CovChartHomeModule } from './home/home.module';
import { CovChartEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { NgxChartsModule } from '@swimlane/ngx-charts';

@NgModule({
  imports: [
    BrowserModule,
    CovChartSharedModule,
    CovChartCoreModule,
    CovChartHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    CovChartEntityModule,
    CovChartAppRoutingModule,
    NgxChartsModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent]
})
export class CovChartAppModule {}
