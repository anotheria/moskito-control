
import {Component, Input} from "@angular/core";
import {Category} from "../entities/category";


@Component({
  selector: 'categories',
  templateUrl: 'categories.component.html'
})
export class CategoriesComponent {

  @Input()
  categories: Category[];

}
