import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { GitHubService } from 'app/shared/util/git-hub.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  files: string[] = [];
  loadingFiles = false;

  constructor(private accountService: AccountService, private loginModalService: LoginModalService, private githubService: GitHubService) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  uploadFiles(): void {
    /* eslint-disable no-console */
    console.log('Clicked');
    /* eslint-enable no-console */

    this.loadingFiles = true;
    this.githubService.get().subscribe(() => (this.loadingFiles = false));
  }
}
