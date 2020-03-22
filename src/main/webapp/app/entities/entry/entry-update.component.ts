import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IEntry, Entry } from 'app/shared/model/entry.model';
import { EntryService } from './entry.service';

@Component({
  selector: 'jhi-entry-update',
  templateUrl: './entry-update.component.html'
})
export class EntryUpdateComponent implements OnInit {
  isSaving = false;
  lastUpdateDp: any;

  editForm = this.fb.group({
    id: [],
    province: [],
    country: [],
    lastUpdate: [],
    confirmed: [],
    deaths: [],
    recovered: [],
    lat: [],
    lon: []
  });

  constructor(protected entryService: EntryService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entry }) => {
      this.updateForm(entry);
    });
  }

  updateForm(entry: IEntry): void {
    this.editForm.patchValue({
      id: entry.id,
      province: entry.province,
      country: entry.country,
      lastUpdate: entry.lastUpdate,
      confirmed: entry.confirmed,
      deaths: entry.deaths,
      recovered: entry.recovered,
      lat: entry.lat,
      lon: entry.lon
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const entry = this.createFromForm();
    if (entry.id !== undefined) {
      this.subscribeToSaveResponse(this.entryService.update(entry));
    } else {
      this.subscribeToSaveResponse(this.entryService.create(entry));
    }
  }

  private createFromForm(): IEntry {
    return {
      ...new Entry(),
      id: this.editForm.get(['id'])!.value,
      province: this.editForm.get(['province'])!.value,
      country: this.editForm.get(['country'])!.value,
      lastUpdate: this.editForm.get(['lastUpdate'])!.value,
      confirmed: this.editForm.get(['confirmed'])!.value,
      deaths: this.editForm.get(['deaths'])!.value,
      recovered: this.editForm.get(['recovered'])!.value,
      lat: this.editForm.get(['lat'])!.value,
      lon: this.editForm.get(['lon'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEntry>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
