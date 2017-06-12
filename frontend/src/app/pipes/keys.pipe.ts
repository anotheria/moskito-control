import { PipeTransform, Pipe } from "@angular/core";


/**
 * Presents object as array of key - value pairs.
 * Used mainly in ngFor directive to iterate over objects.
 *
 * @author strel
 */
@Pipe({ name: 'keys' })
export class KeysPipe implements PipeTransform {
  transform(value, args: string[]): any {
    let keys = [];
    for (let key in value) {
      keys.push({ key: key, value: value[key] });
    }
    return keys;
  }
}
