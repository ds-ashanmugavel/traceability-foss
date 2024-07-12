import { APP_INITIALIZER } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, convertToParamMap, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { PoliciesFacade } from '@page/admin/presentation/policy-management/policies/policies.facade';
import { PoliciesAssembler } from '@page/admin/presentation/policy-management/policies/policy.assembler';
import { OperatorType, Policy, PolicyAction } from '@page/policies/model/policy.model';
import { ToastService } from '@shared/components/toasts/toast.service';
import { ViewMode } from '@shared/model/view.model';
import { renderComponent } from '@tests/test-render.utils';
import { I18NEXT_SERVICE, ITranslationService } from 'angular-i18next';
import { of, Subject } from 'rxjs';
import { PolicyEditorComponent } from './policy-editor.component';

describe('PolicyEditorComponent', () => {
  let mockPoliciesFacade: Partial<PoliciesFacade>;
  let mockToastService: Partial<ToastService>;
  let mockRouter: Partial<Router>;
  let mockRoute: Partial<ActivatedRoute>;
  let selectedPolicySubject: Subject<{ data: Policy }>;

  const mockPolicy: Policy = {
    policyName: 'policy123',
    policyId: 'policy123',
    createdOn: '2024-01-01T00:00:00Z',
    validUntil: '2024-12-31T23:59:59Z',
    bpn: 'Test BPN',
    permissions: [
      {
        action: 'use' as PolicyAction,
        constraint: {
          and: [
            {
              leftOperand: 'left1',
              operator: { '@id': OperatorType.EQ },
              operatorTypeResponse: OperatorType.EQ,
              'rightOperand': 'right1',
            },
          ],
          or: [
            {
              leftOperand: 'left2',
              operator: { '@id': OperatorType.NEQ },
              operatorTypeResponse: OperatorType.NEQ,
              'rightOperand': 'right2',
            },
          ],
        },
      },
    ],
    constraints: null,
  };

  beforeEach(() => {
    selectedPolicySubject = new Subject();
    mockPoliciesFacade = {
      selectedPolicy$: selectedPolicySubject.asObservable(),
      setSelectedPolicyById: jasmine.createSpy(),
      createPolicy: jasmine.createSpy().and.returnValue(of({})),
      updatePolicy: jasmine.createSpy().and.returnValue(of({})),
      setPolicies: jasmine.createSpy(),
    };
    mockToastService = {
      success: jasmine.createSpy(),
      error: jasmine.createSpy(),
    };
    mockRouter = {
      navigate: jasmine.createSpy(),
      url: 'admin/policies/create',
    };


    mockRoute = {
      snapshot: {
        paramMap: convertToParamMap({ policyId: '1' }),
        url: [],
        params: {},
        queryParams: {},
        fragment: null,
        data: {},
        outlet: 'primary',
        component: PolicyEditorComponent,
        root: null,
        parent: null,
        firstChild: null,
        children: [],
        pathFromRoot: [],
        toString: () => '',
        routeConfig: null,
        title: '',
        queryParamMap: convertToParamMap({}),
      },
    };
  });

  const renderPolicyEditorComponent = () =>
    renderComponent(PolicyEditorComponent, {
      imports: [ RouterTestingModule ],
      providers: [
        { provide: PoliciesFacade, useValue: mockPoliciesFacade },
        { provide: ToastService, useValue: mockToastService },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockRoute },
        FormBuilder,
        {
          provide: APP_INITIALIZER,
          useFactory: (i18next: ITranslationService) => {
            return () =>
              i18next.init({
                lng: 'en',
                supportedLngs: [ 'en', 'de' ],
                resources: {},
              });
          },
          deps: [ I18NEXT_SERVICE ],
          multi: true,
        },
      ],
    });

  it('should create', async () => {
    const { fixture } = await renderPolicyEditorComponent();
    const { componentInstance } = fixture;
    expect(componentInstance).toBeTruthy();
  });

  it('should initialize the form in create mode', async () => {
    const { fixture } = await renderPolicyEditorComponent();
    const { componentInstance } = fixture;
    expect(componentInstance.viewMode).toBe(ViewMode.CREATE);
    expect(componentInstance.policyForm).toBeTruthy();
    expect(componentInstance.policyForm.get('policyName').valid).toBeFalsy(); // Validators required
    expect(componentInstance.policyForm.get('bpns').valid).toBeFalsy(); // Validators required
    expect(componentInstance.policyForm.get('validUntil').valid).toBeFalsy(); // Validators required
  });

  it('should initialize the view mode correctly', async () => {
    const { fixture } = await renderPolicyEditorComponent();
    const { componentInstance } = fixture;
    Object.defineProperty(mockRouter, 'url', {
      get: jasmine.createSpy().and.returnValue('admin/policies/create'),
    });
    expect(componentInstance.initializeViewMode()).toBe(ViewMode.CREATE);
    Object.defineProperty(mockRouter, 'url', {
      get: jasmine.createSpy().and.returnValue('admin/policies/edit/1'),
    });

    expect(componentInstance.initializeViewMode()).toBe(ViewMode.EDIT);

    Object.defineProperty(mockRouter, 'url', {
      get: jasmine.createSpy().and.returnValue('admin/policies/1'),
    });

    expect(componentInstance.initializeViewMode()).toBe(ViewMode.VIEW);
  });

  it('should add and remove constraints', async () => {
    const { fixture } = await renderPolicyEditorComponent();
    const { componentInstance } = fixture;
    expect(componentInstance.constraints.length).toBe(1);

    componentInstance.addConstraintFormGroup();
    expect(componentInstance.constraints.length).toBe(2);

    componentInstance.removeConstraintFormGroup(0);
    expect(componentInstance.constraints.length).toBe(1);
  });

  it('should move constraints up and down', async () => {
    const { fixture } = await renderPolicyEditorComponent();
    const { componentInstance } = fixture;
    componentInstance.addConstraintFormGroup();
    componentInstance.addConstraintFormGroup();

    componentInstance.constraints.at(0).get('leftOperand').setValue('constraint 1');
    componentInstance.constraints.at(1).get('leftOperand').setValue('constraint 2');

    componentInstance.moveConstraintDown(0);
    expect(componentInstance.constraints.at(0).get('leftOperand').value).toBe('constraint 2');
    expect(componentInstance.constraints.at(1).get('leftOperand').value).toBe('constraint 1');

    componentInstance.moveConstraintUp(1);
    expect(componentInstance.constraints.at(0).get('leftOperand').value).toBe('constraint 1');
    expect(componentInstance.constraints.at(1).get('leftOperand').value).toBe('constraint 2');
  });

  it('should navigate back', async () => {
    const { fixture } = await renderPolicyEditorComponent();
    const { componentInstance } = fixture;
    componentInstance.navigateBack();
    expect(mockRouter.navigate).toHaveBeenCalledWith([ 'admin/policies' ]);
  });

  it('should save policy in create mode', async () => {
    const { fixture } = await renderPolicyEditorComponent();
    const { componentInstance } = fixture;
    componentInstance.policyForm.patchValue({
      policyName: 'Test Policy',
      validUntil: new Date().toISOString(),
      bpns: 'BPN0001',
      accessType: 'access',
      constraintLogicType: 'AND',
    });
    componentInstance.addConstraintFormGroup();
    componentInstance.constraints.at(0).patchValue({
      leftOperand: 'leftOperand',
      operator: '=',
      rightOperand: 'rightOperand',
    });

    componentInstance.savePolicy();
    expect(mockPoliciesFacade.createPolicy).toHaveBeenCalled();
    expect(mockToastService.success).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalled();
  });

  it('should update policy form correctly in create mode', async () => {
    const { fixture } = await renderPolicyEditorComponent();
    const { componentInstance } = fixture;
    componentInstance.selectedPolicy = mockPolicy;
    const policy: Policy = PoliciesAssembler.assemblePolicy(mockPolicy);

    componentInstance.viewMode = ViewMode.CREATE;
    componentInstance.policyForm = componentInstance.fb.group({
      policyName: '',
      validUntil: null,
      bpns: '',
      accessType: '',
      constraintLogicType: '',
      constraints: componentInstance.fb.array([]),
    });

    componentInstance.updatePolicyForm(policy);

    // Assert the form values are updated correctly
    expect(componentInstance.policyForm.getRawValue().policyName).toBe('policy123');
    expect(componentInstance.policyForm.getRawValue().validUntil).toBe(policy.validUntil);
    expect(componentInstance.policyForm.getRawValue().bpns).toBe('Test BPN');
    expect(componentInstance.policyForm.getRawValue().accessType).toBe('USE');
    expect(componentInstance.policyForm.getRawValue().constraintLogicType).toBe('AND');
    expect(componentInstance.policyForm.getRawValue().constraints.length).toBe(1);

    componentInstance.viewMode = ViewMode.VIEW;

    componentInstance.updatePolicyForm(policy);
    expect(componentInstance.policyForm.disabled).toBe(true);

    componentInstance.viewMode = ViewMode.EDIT;
    componentInstance.updatePolicyForm(policy);
    expect(componentInstance.policyForm.get('validUntil').disabled).toBe(false);
    expect(componentInstance.policyForm.get('bpns').disabled).toBe(false);
    expect(componentInstance.constraints.disabled).toBe(true);

  });

  it('should mark all form controls and form array elements as touched', async () => {
    const { fixture } = await renderPolicyEditorComponent();
    const { componentInstance } = fixture;
    componentInstance.policyForm = componentInstance.fb.group({
      policyName: [ '', Validators.required ],
      constraints: componentInstance.fb.array([
        componentInstance.fb.group({
          leftOperand: [ '', Validators.required ],
          operator: [ '', Validators.required ],
          rightOperand: [ '', Validators.required ],
        }),
      ]),
    });

    spyOn(componentInstance.policyForm.get('policyName'), 'markAsTouched');
    spyOn(componentInstance.constraints.at(0).get('leftOperand'), 'markAsTouched');

    componentInstance.validateAllFields();

    expect(componentInstance.policyForm.get('policyName').markAsTouched).toHaveBeenCalled();
    expect(componentInstance.constraints.at(0).get('leftOperand').markAsTouched).toHaveBeenCalled();
  });

  it('should mark all elements in a FormArray as touched', async () => {
    const { fixture } = await renderPolicyEditorComponent();
    const { componentInstance } = fixture;
    componentInstance.policyForm = componentInstance.fb.group({
      constraints: componentInstance.fb.array([
        componentInstance.fb.group({
          leftOperand: [ '', Validators.required ],
          operator: [ '', Validators.required ],
          rightOperand: [ '', Validators.required ],
        }),
        componentInstance.fb.control(''),
      ]),
    });

    const formArray = componentInstance.policyForm.get('constraints') as FormArray;
    spyOn(formArray.at(0).get('leftOperand'), 'markAsTouched');
    spyOn(formArray.at(1), 'markAsTouched');

    componentInstance.validateFormArray(formArray);

    expect(formArray.at(0).get('leftOperand').markAsTouched).toHaveBeenCalled();
    expect(formArray.at(1).markAsTouched).toHaveBeenCalled();
  });

  it('should mark all fields in a FormGroup within a FormArray as touched', async () => {
    const { fixture } = await renderPolicyEditorComponent();
    const { componentInstance } = fixture;
    componentInstance.policyForm = componentInstance.fb.group({
      constraints: componentInstance.fb.array([
        componentInstance.fb.group({
          leftOperand: [ '', Validators.required ],
          operator: [ '', Validators.required ],
          rightOperand: [ '', Validators.required ],
        }),
      ]),
    });

    const formGroup = componentInstance.constraints.at(0) as FormGroup;
    spyOn(formGroup.get('leftOperand'), 'markAsTouched');
    spyOn(formGroup.get('operator'), 'markAsTouched');
    spyOn(formGroup.get('rightOperand'), 'markAsTouched');

    componentInstance.validateAllFieldsInFormGroup(formGroup);

    expect(formGroup.get('leftOperand').markAsTouched).toHaveBeenCalled();
    expect(formGroup.get('operator').markAsTouched).toHaveBeenCalled();
    expect(formGroup.get('rightOperand').markAsTouched).toHaveBeenCalled();
  });

});
