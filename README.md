#What is MoSKito-Control?
  
**MoSKito-Control monitors performance of multi-node web applications.**

MoSKito-Control is a standalone application, although it needs MoSKito-Core to actually get the performance data.

#Why is it good?

* From a single glance, you get a complete picture of your app's health.
* You may build combined performance charts with data, taken from multiple nodes.
* The entire health change history is available in a click.
* You can mute monitoring while you're deploying or fixing things, and thus not receive false alarms.
* [iPhone app](https://itunes.apple.com/en/app/msk-control/id688838411?mt=8).

## User Interface

**UI is MoSKito-Central's strongest point.**

It presents *'a bird's eye view'* on the monitored application, giving a clear picture of the app's health for both Developers and Managers, predicting any possible or upcoming problem.

![MoSKito-Control](https://github.com/anotheria/moskito-control/blob/master/docs/images/components_ok_not_ok.png?raw=true)

The interface feels like a console, filled with leds (light bulbs) that change colors. Every led is an application component, its color is a certain health state.

As soon as component's performance changes, the health indicator changes color. This lets you see the problem instantly and react immediately, before the appearing issue affects the whole app.

# How does it work?

MoSKito-Control has 2 main elements:

1. **Agent** (installed into the monitored application), 
2. **Server** (WebApp).

**Agents** fetch performance info from MoSKito-Core and send it to Server via HTTP. **Server** receives data from multiple Agents, consolidates, processes and displays it through **UI**.

![MoSKito-Control data flow](https://confluence.opensource.anotheria.net/download/attachments/25100513/moskito-control_overview.png)

#Install & Config

1. Install agent and set up webapp ([read the guide](https://confluence.opensource.anotheria.net/display/MSK/MoSKito-Control+Quick+Setup+Guide)).
2. Enter addresses of your machines, build the *war* and put it into another Tomcat.

Enjoy!

#[ChangeLog](https://confluence.opensource.anotheria.net/display/MSK/MoSKito-Control+Changelog)

#License

MoSKito-Control, as well as other MoSKito Projects, is free and open source (MIT License). Use it as you wish.

[Read License](https://github.com/anotheria/moskito/blob/master/LICENSE)

# Project resources

#### [Webpage](http://www.moskito.org/moskito-control.html)
#### [Documentation](https://confluence.opensource.anotheria.net/display/MSK/MoSKito-Control)
#### [MoSKito FAQ](https://confluence.opensource.anotheria.net/display/MSK/MoSKito+FAQ)

# Support and feedback

**We're willing to help everyone.**

For any questions, write to [moskito@anotheria.net](mailto: moskito@anotheria.net).