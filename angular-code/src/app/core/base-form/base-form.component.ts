import {Directive, OnInit} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup} from '@angular/forms';

@Directive()
export abstract class BaseFormComponent implements OnInit {
  form!: UntypedFormGroup;

  protected constructor(private readonly formBuilder: UntypedFormBuilder) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group(this.setupForm(this.formBuilder));
  }

  abstract submit(): void;

  protected abstract setupForm(formBuilder: UntypedFormBuilder): any;
}
