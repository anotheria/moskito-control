import {Component, Input} from '@angular/core';
import {ITimer} from './itimer';


@Component({
  selector: 'timer',
  templateUrl: 'timer.component.html'
})
export class TimerComponent {

  @Input()
  public timeInSeconds: number;

  @Input()
  public callback: () => void;

  public timer: ITimer;
  public lastRefreshTimestamp: Date;


  constructor() {
  }

  ngOnInit() {
    this.initTimer();
  }

  hasFinished() {
    return this.timer.hasFinished;
  }

  initTimer() {
    if(!this.timeInSeconds) {
      this.timeInSeconds = 0;
    }

    this.timer = <ITimer>{
      seconds: this.timeInSeconds,
      runTimer: false,
      hasStarted: false,
      hasFinished: false,
      secondsRemaining: this.timeInSeconds
    };

    this.lastRefreshTimestamp = new Date();
  }

  startTimer() {
    this.timer.hasStarted = true;
    this.timer.runTimer = true;
    this.timerTick();
  }

  restartTimer() {
    this.initTimer();
    this.startTimer();
  }

  pauseTimer() {
    this.timer.runTimer = false;
  }

  resumeTimer() {
    this.startTimer();
  }

  timerTick() {
    setTimeout(() => {
      if (!this.timer.runTimer) { return; }
      this.timer.secondsRemaining--;
      if (this.timer.secondsRemaining > 0) {
        this.timerTick();
      }
      else {
        this.timer.hasFinished = true;
        this.callback();
        this.restartTimer();
      }
    }, 1000);
  }
}
