import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { GitHubService } from 'app/shared/util/git-hub.service';
import { LineChartDataService } from 'app/home/line-chart/line-chart-data.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.scss']
})
export class LineChartComponent implements OnInit, OnDestroy, AfterViewInit {
  dataSub!: Subscription;

  multi: any[] = [];

  // options
  legend = true;
  showLabels = true;
  animations = true;
  xAxis = true;
  yAxis = true;
  showYAxisLabel = true;
  showXAxisLabel = true;
  xAxisLabel = 'Date';
  yAxisLabel = 'Confirmed';
  timeline = true;

  colorScheme = {
    domain: ['#5AA454', '#E44D25', '#CFC0BB', '#7aa3e5', '#a8385d', '#aae3f5']
  };

  constructor(private githubService: GitHubService, private lineChartDataService: LineChartDataService) {}

  ngOnInit(): void {
    this.dataSub = this.lineChartDataService.dataSubject.subscribe(e => {
      this.multi = e;
    });
  }

  ngOnDestroy(): void {
    this.dataSub.unsubscribe();
  }

  showChart(): boolean {
    return true;
  }

  onSelect(data: string): void {
    //console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  onActivate(data: string): void {
    //console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data: string): void {
    //console.log('Deactivate', JSON.parse(JSON.stringify(data)));
  }

  ngAfterViewInit(): void {
    this.multi = [...this.multi];
  }
}
