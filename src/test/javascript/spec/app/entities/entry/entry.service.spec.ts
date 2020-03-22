import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { EntryService } from 'app/entities/entry/entry.service';
import { IEntry, Entry } from 'app/shared/model/entry.model';

describe('Service Tests', () => {
  describe('Entry Service', () => {
    let injector: TestBed;
    let service: EntryService;
    let httpMock: HttpTestingController;
    let elemDefault: IEntry;
    let expectedResult: IEntry | IEntry[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(EntryService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Entry('ID', 'AAAAAAA', 'AAAAAAA', currentDate, 0, 0, 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            lastUpdate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find('123').subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Entry', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            lastUpdate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastUpdate: currentDate
          },
          returnedFromService
        );

        service.create(new Entry()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Entry', () => {
        const returnedFromService = Object.assign(
          {
            province: 'BBBBBB',
            country: 'BBBBBB',
            lastUpdate: currentDate.format(DATE_FORMAT),
            confirmed: 1,
            deaths: 1,
            recovered: 1,
            lat: 1,
            lon: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastUpdate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Entry', () => {
        const returnedFromService = Object.assign(
          {
            province: 'BBBBBB',
            country: 'BBBBBB',
            lastUpdate: currentDate.format(DATE_FORMAT),
            confirmed: 1,
            deaths: 1,
            recovered: 1,
            lat: 1,
            lon: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastUpdate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Entry', () => {
        service.delete('123').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
