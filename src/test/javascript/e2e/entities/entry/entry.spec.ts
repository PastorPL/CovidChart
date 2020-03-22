import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EntryComponentsPage, EntryDeleteDialog, EntryUpdatePage } from './entry.page-object';

const expect = chai.expect;

describe('Entry e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let entryComponentsPage: EntryComponentsPage;
  let entryUpdatePage: EntryUpdatePage;
  let entryDeleteDialog: EntryDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Entries', async () => {
    await navBarPage.goToEntity('entry');
    entryComponentsPage = new EntryComponentsPage();
    await browser.wait(ec.visibilityOf(entryComponentsPage.title), 5000);
    expect(await entryComponentsPage.getTitle()).to.eq('covChartApp.entry.home.title');
    await browser.wait(ec.or(ec.visibilityOf(entryComponentsPage.entities), ec.visibilityOf(entryComponentsPage.noResult)), 1000);
  });

  it('should load create Entry page', async () => {
    await entryComponentsPage.clickOnCreateButton();
    entryUpdatePage = new EntryUpdatePage();
    expect(await entryUpdatePage.getPageTitle()).to.eq('covChartApp.entry.home.createOrEditLabel');
    await entryUpdatePage.cancel();
  });

  it('should create and save Entries', async () => {
    const nbButtonsBeforeCreate = await entryComponentsPage.countDeleteButtons();

    await entryComponentsPage.clickOnCreateButton();

    await promise.all([
      entryUpdatePage.setProvinceInput('province'),
      entryUpdatePage.setCountryInput('country'),
      entryUpdatePage.setLastUpdateInput('2000-12-31'),
      entryUpdatePage.setConfirmedInput('5'),
      entryUpdatePage.setDeathsInput('5'),
      entryUpdatePage.setRecoveredInput('5'),
      entryUpdatePage.setLatInput('5'),
      entryUpdatePage.setLonInput('5')
    ]);

    expect(await entryUpdatePage.getProvinceInput()).to.eq('province', 'Expected Province value to be equals to province');
    expect(await entryUpdatePage.getCountryInput()).to.eq('country', 'Expected Country value to be equals to country');
    expect(await entryUpdatePage.getLastUpdateInput()).to.eq('2000-12-31', 'Expected lastUpdate value to be equals to 2000-12-31');
    expect(await entryUpdatePage.getConfirmedInput()).to.eq('5', 'Expected confirmed value to be equals to 5');
    expect(await entryUpdatePage.getDeathsInput()).to.eq('5', 'Expected deaths value to be equals to 5');
    expect(await entryUpdatePage.getRecoveredInput()).to.eq('5', 'Expected recovered value to be equals to 5');
    expect(await entryUpdatePage.getLatInput()).to.eq('5', 'Expected lat value to be equals to 5');
    expect(await entryUpdatePage.getLonInput()).to.eq('5', 'Expected lon value to be equals to 5');

    await entryUpdatePage.save();
    expect(await entryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await entryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Entry', async () => {
    const nbButtonsBeforeDelete = await entryComponentsPage.countDeleteButtons();
    await entryComponentsPage.clickOnLastDeleteButton();

    entryDeleteDialog = new EntryDeleteDialog();
    expect(await entryDeleteDialog.getDialogTitle()).to.eq('covChartApp.entry.delete.question');
    await entryDeleteDialog.clickOnConfirmButton();

    expect(await entryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
