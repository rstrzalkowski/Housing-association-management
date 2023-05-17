import { Component, Input } from '@angular/core';
import { AccessLevel } from '../../../model/account';
import { AccessType } from '../../../model/access-type';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, NonNullableFormBuilder, Validators } from '@angular/forms';
import { AccessLevelService } from '../../../services/access-level.service';
import { ToastService } from '../../../services/toast.service';
import { ConfirmActionComponent } from '../confirm-action/confirm-action.component';
import { filter } from 'rxjs';

@Component({
    selector: 'app-grant-access-level',
    templateUrl: './grant-access-level.component.html'
})
export class GrantAccessLevelComponent {
    protected readonly AccessTypeEnum = AccessType;
    private _accessType!: AccessType;

    /**
     * Specifies whether the modal is used to add new access level or to verfiy existing one.
     */
    @Input()
    grantNewAccessLevelMode = true;

    @Input()
    id!: number;

    private _accessLevel: AccessLevel | null = null;

    @Input()
    set accessLevel(value: AccessLevel | null) {
        if (value) {
            this._accessLevel = value;

            this.ownerOrManagerForm.patchValue({
                address: value.address,
                licenseNumber: value.licenseNumber
            });
            this.ownerOrManagerForm.disable();
        }
    }

    @Input()
    set accessType(value) {
        this._accessType = value;
        this.ownerOrManagerForm.enable();
        switch (value) {
            case AccessType.ADMIN:
                this.ownerOrManagerForm.disable();
                break;
            case AccessType.MANAGER:
                this.licenseNumberControl.enable();
                break;
            case AccessType.OWNER:
                this.licenseNumberControl.disable();
                break;
        }
    }

    get accessType() {
        return this._accessType;
    }

    constructor(
        protected activeModal: NgbActiveModal,
        private fb: NonNullableFormBuilder,
        private accessLevelService: AccessLevelService,
        private toastService: ToastService,
        private modalService: NgbModal
    ) {}

    protected ownerOrManagerForm = new FormGroup({
        address: this.fb.group({
            postalCode: this.fb.control('', {
                validators: [
                    Validators.required,
                    Validators.minLength(6),
                    Validators.maxLength(6)
                ]
            }),
            city: this.fb.control('', {
                validators: [
                    Validators.required,
                    Validators.minLength(2),
                    Validators.maxLength(85)
                ]
            }),
            street: this.fb.control('', {
                validators: [Validators.required, Validators.maxLength(85)]
            }),
            buildingNumber: this.fb.control(0, {
                validators: [Validators.required, Validators.min(1)]
            })
        }),
        licenseNumber: this.fb.control('', {
            validators: [Validators.required]
        })
    });

    grant() {
        const confirmModal = this.modalService.open(ConfirmActionComponent);
        confirmModal.componentInstance.message =
            'Potwierdź nadanie poziomu dostępu'; // TODO translate
        confirmModal.componentInstance.danger = `access-levels.${this.accessType}`;

        confirmModal.closed.pipe(filter((res) => res)).subscribe(() => {
            let dto = undefined;

            switch (this._accessType) {
                case AccessType.MANAGER:
                    dto = this.ownerOrManagerForm.getRawValue();
                    break;
                case AccessType.OWNER:
                    dto = {
                        address: this.ownerOrManagerForm.getRawValue().address
                    };
                    break;
            }

            this.accessLevelService
                .grantAccessLevel(this.id, this._accessType, dto)
                .subscribe((message) => {
                    if (!message) {
                        this.toastService.showSuccess(
                            'Pomyślnie nadano poziom dostępu'
                        ); // TODO translate
                        this.activeModal.close();
                    } else {
                        this.toastService.showDanger(message); // TODO translate
                    }
                });
        });
    }

    reject() {
        this.accessLevelService
            .reject(this.id, this.accessType)
            .subscribe((success) => {
                if (success) {
                    this.toastService.showSuccess(
                        'Operacja zakończona pomyślnie'
                    );
                    this.activeModal.close();
                }
            });
    }

    protected get postalCodeControl() {
        return this.ownerOrManagerForm.controls.address.controls.postalCode;
    }

    protected get cityControl() {
        return this.ownerOrManagerForm.controls.address.controls.city;
    }

    protected get streetControl() {
        return this.ownerOrManagerForm.controls.address.controls.street;
    }

    protected get buildingNumberControl() {
        return this.ownerOrManagerForm.controls.address.controls.buildingNumber;
    }

    protected get licenseNumberControl() {
        return this.ownerOrManagerForm.controls.licenseNumber;
    }
}
