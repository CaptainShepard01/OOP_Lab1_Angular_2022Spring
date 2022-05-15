import { Injectable } from '@angular/core';
import {FormGroup} from "@angular/forms";

@Injectable({
  providedIn: 'root'
})
export class FieldValidatorService {
// @ts-ignore
  form: FormGroup;
  // @ts-ignore
  private formSubmitAttempt: boolean;

  constructor() { }

  ngOnInit(): void {
  }

  isFieldValid(field: string) {
    // @ts-ignore
    return (!this.form.get(field).valid && this.form.get(field).touched) || (this.form.get(field).untouched && this.formSubmitAttempt);
  }

  displayFieldCss(field: string) {
    return {
      'has-error': this.isFieldValid(field),
      'has-feedback': this.isFieldValid(field)
    };
  }

  reset() {
    this.form.reset();
    this.formSubmitAttempt = false;
  }
}
