import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CovChartSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { LineChartComponent } from './line-chart/line-chart.component';
import { CommonModule } from '@angular/common';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  imports: [CommonModule, CovChartSharedModule, RouterModule.forChild([HOME_ROUTE]), NgxChartsModule, BrowserAnimationsModule],
  declarations: [HomeComponent, LineChartComponent]
})
export class CovChartHomeModule {}
