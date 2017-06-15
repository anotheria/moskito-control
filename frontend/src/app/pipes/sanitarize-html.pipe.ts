import { Pipe, PipeTransform } from "@angular/core";
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";


/**
 * Sanitizes HTML layout and produces safe HTML for
 * further embedding in DOM.
 *
 * Used mainly in component inspection modal
 * to sanitize checkboxes for accumulators tab.
 *
 * @author strel
 */
@Pipe({
  name: 'sanitizeHtml'
})
export class SanitizeHtmlPipe implements PipeTransform  {

  constructor(private _sanitizer: DomSanitizer){}

  transform(v: string) : SafeHtml {
    return this._sanitizer.bypassSecurityTrustHtml(v);
  }
}
