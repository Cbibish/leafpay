import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import LoginComponent from './login.component';
import { LoginService } from './login.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ReactiveFormsModule } from '@angular/forms';

describe('LoginComponent - Role Redirection (Jest)', () => {
  let comp: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockRouter: jest.Mocked<Router>;
  let mockLoginService: jest.Mocked<LoginService>;
  let mockAccountService: jest.Mocked<AccountService>;

  beforeEach(async () => {
    mockRouter = {
      navigate: jest.fn(),
    } as unknown as jest.Mocked<Router>;

    mockLoginService = {
      login: jest.fn(),
    } as unknown as jest.Mocked<LoginService>;

    mockAccountService = {
      identity: jest.fn(),
      isAuthenticated: jest.fn(),
    } as unknown as jest.Mocked<AccountService>;

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [LoginComponent],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: LoginService, useValue: mockLoginService },
        { provide: AccountService, useValue: mockAccountService },
      ],
    })
      .overrideTemplate(LoginComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    comp = fixture.componentInstance;
    comp.ngOnInit();
  });

  const fillLoginForm = (username: string, password: string, rememberMe: boolean = false) => {
    comp.loginForm.setValue({ username, password, rememberMe });
  };

  it('should redirect to /admin/admin-dashboard if role is ADMIN', () => {
    const mockAccount: Account = {
      activated: true,
      authorities: ['ROLE_ADMIN'],
      email: 'twinkywinky@yahoo.com',
      firstName: 'twinky',
      lastName: 'winker',
      langKey: 'en',
      login: 'twinkywinky@yahoo.com',
      imageUrl: null,
      role: { id: 1503, nom: 'ADMIN' },
    };

    mockLoginService.login.mockReturnValue(of(mockAccount));
    mockAccountService.identity.mockReturnValue(of(mockAccount));

    fillLoginForm('twinkywinky@yahoo.com', 'rootah');

    comp.login();

    expect(mockLoginService.login).toHaveBeenCalledWith({
      username: 'twinkywinky@yahoo.com',
      password: 'rootah',
      rememberMe: false,
    });

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/admin/admin-dashboard']);
  });

  it('should redirect to /client-dashboard if role is NORMAL_USER', () => {
    const mockAccount: Account = {
      activated: true,
      authorities: ['ROLE_USER'],
      email: 'client@example.com',
      firstName: 'John',
      lastName: 'Doe',
      langKey: 'fr',
      login: 'client@example.com',
      imageUrl: null,
      role: { id: 1234, nom: 'NORMAL_USER' },
    };

    mockLoginService.login.mockReturnValue(of(mockAccount));
    mockAccountService.identity.mockReturnValue(of(mockAccount));

    fillLoginForm('client@example.com', 'rootah', true);

    comp.login();

    expect(mockLoginService.login).toHaveBeenCalledWith({
      username: 'client@example.com',
      password: 'rootah',
      rememberMe: true,
    });

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/client-dashboard']);
  });

  it('should redirect to / if role is unknown', () => {
    const mockAccount: Account = {
      activated: true,
      authorities: ['ROLE_USER'],
      email: 'random@example.com',
      firstName: 'Jane',
      lastName: 'Doe',
      langKey: 'en',
      login: 'random@example.com',
      imageUrl: null,
      role: { id: 9999, nom: 'UNKNOWN_ROLE' },
    };

    mockLoginService.login.mockReturnValue(of(mockAccount));
    mockAccountService.identity.mockReturnValue(of(mockAccount));

    fillLoginForm('random@example.com', 'pass');

    comp.login();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });

  it('should set authenticationError on login failure', () => {
    mockLoginService.login.mockReturnValue(throwError(() => new Error('Login failed')));

    fillLoginForm('invalid@example.com', 'wrong');

    comp.login();

    expect(comp.authenticationError()).toBe(true);
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });
});
