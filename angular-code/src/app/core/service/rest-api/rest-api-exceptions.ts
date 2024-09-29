import {HttpRequest} from '@angular/common/http';


const requestsWithoutGlobalspinner = [''];

export const shouldShowSpinnerOnRequest = (req: HttpRequest<any>): boolean =>
  !requestsWithoutGlobalspinner.find((httpReqPattern) => new RegExp(httpReqPattern).test(req.url));
