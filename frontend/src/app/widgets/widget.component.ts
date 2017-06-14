

export abstract class Widget {

  name: string;
  displayName: string;
  className: string;
  icon: string;
  enabled: boolean;


  abstract refresh();

}
