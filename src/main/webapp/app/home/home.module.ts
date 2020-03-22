import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CovChartSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { LineChartComponent } from './line-chart/line-chart.component';
import { ChartsModule } from 'ng2-charts';

@NgModule({
  imports: [CovChartSharedModule, RouterModule.forChild([HOME_ROUTE]), ChartsModule],
  declarations: [HomeComponent, LineChartComponent]
})
export class CovChartHomeModule {}
