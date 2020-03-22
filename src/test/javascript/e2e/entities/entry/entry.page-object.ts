import { element, by, ElementFinder } from 'protractor';

export class EntryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-entry div table .btn-danger'));
  title = element.all(by.css('jhi-entry div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class EntryUpdatePage {
  pageTitle = element(by.id('jhi-entry-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  provinceInput = element(by.id('field_province'));
  countryInput = element(by.id('field_country'));
  lastUpdateInput = element(by.id('field_lastUpdate'));
  confirmedInput = element(by.id('field_confirmed'));
  deathsInput = element(by.id('field_deaths'));
  recoveredInput = element(by.id('field_recovered'));
  latInput = element(by.id('field_lat'));
  lonInput = element(by.id('field_lon'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setProvinceInput(province: string): Promise<void> {
    await this.provinceInput.sendKeys(province);
  }

  async getProvinceInput(): Promise<string> {
    return await this.provinceInput.getAttribute('value');
  }

  async setCountryInput(country: string): Promise<void> {
    await this.countryInput.sendKeys(country);
  }

  async getCountryInput(): Promise<string> {
    return await this.countryInput.getAttribute('value');
  }

  async setLastUpdateInput(lastUpdate: string): Promise<void> {
    await this.lastUpdateInput.sendKeys(lastUpdate);
  }

  async getLastUpdateInput(): Promise<string> {
    return await this.lastUpdateInput.getAttribute('value');
  }

  async setConfirmedInput(confirmed: string): Promise<void> {
    await this.confirmedInput.sendKeys(confirmed);
  }

  async getConfirmedInput(): Promise<string> {
    return await this.confirmedInput.getAttribute('value');
  }

  async setDeathsInput(deaths: string): Promise<void> {
    await this.deathsInput.sendKeys(deaths);
  }

  async getDeathsInput(): Promise<string> {
    return await this.deathsInput.getAttribute('value');
  }

  async setRecoveredInput(recovered: string): Promise<void> {
    await this.recoveredInput.sendKeys(recovered);
  }

  async getRecoveredInput(): Promise<string> {
    return await this.recoveredInput.getAttribute('value');
  }

  async setLatInput(lat: string): Promise<void> {
    await this.latInput.sendKeys(lat);
  }

  async getLatInput(): Promise<string> {
    return await this.latInput.getAttribute('value');
  }

  async setLonInput(lon: string): Promise<void> {
    await this.lonInput.sendKeys(lon);
  }

  async getLonInput(): Promise<string> {
    return await this.lonInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class EntryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-entry-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-entry'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
