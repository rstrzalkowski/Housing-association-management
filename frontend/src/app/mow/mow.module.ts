import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { MowRoutingModule } from './mow-routing.module';
import { SharedModule } from '../shared/shared.module';
import { PlaceCategoriesComponent } from './components/place-categories/place-categories.component';
import { PlaceDetailsComponent } from './components/place-details/place-details.component';

@NgModule({
    declarations: [PlaceCategoriesComponent, PlaceDetailsComponent],
    imports: [CommonModule, MowRoutingModule, SharedModule]
})
export class MowModule {}
