[MoSKito Control](https://confluence.opensource.anotheria.net/display/MSK/MoSKito+Control)
===============

## Definition
  
**MoSKito Control is a tool for monitoring performance of multi-node web applications.**

MoSKito Control (a part of [MoSKito Framework](http://www.moskito.org)) is a standalone web-based real-time performance monitoring tool, specially designed for watching multi-server apps. It is extremely flexible, easy to set up and does not take much system resources. 

One of MoSKito Control's greatest advantages is its user interface: browser-based, minimalistic, intuitive and convenient.

*Presenting "a bird's eye view" of the monitored applications, MoSKito Control gives a clear picture of the app's health for both Developers and Managers, predicting any possible or upcoming problem.*

## Mechanics ##

MoSKito Control consists of two parts:

1. The **agent**, which is installed into the target (monitored) application, 
2. **WebApp** (Server).

**Agents** collect the performance info and send it to WebApp via HTTP. **WebApp** receives the data, handles it and displays the processed info through **user interface**.

### WebApp User Interface

WebApp's user interface is handcrafted to provide full control to all aspects of a web application, becoming a universal monitoring station with maximum convenience for the user.

#### How It Works

1. Every **application**, watched by MoSKito Control, consists of monitored **components** (functional application parts, monitored by **Agents**).

2. **Widgets** display essential components' info: health **status** (a color for every health state), performance **charts**, **history** of health changes.

3. Components may be filtered by **categories** or and current **statistics**.

![image](https://github.com/anotheria/moskito-control/blob/master/docs/images/components_ok_not_ok.png?raw=true)

#### How It Feels
The interface feels like a console, filled with leds (light bulbs) that change colors. Every led is an application component, its color is a certain health state.

As soon as component's performance changes, the health indicator changes color. This lets you see the problem instantly and react immediately, before the appearing issue affects the whole app.

## Guides and Manuals ##

1. [MoSKito Control Quick Setup Guide](https://confluence.opensource.anotheria.net/display/MSK/MoSKito+Control+Quick+Setup+Guide) describes how to connect MoSKito Control with your application: install agents and configure Control web app itself.

2. [MoSKito Control Web App User Manual](https://confluence.opensource.anotheria.net/display/MSK/MoSKito+Control+Web+App+User+Manual) is a guide to the web app's user interface.

## History and Current State ##

Originally developed as Healthcheck Monitor for the PARSHIP GmbH, it has been contributed back to MoSKito codebase by PARSHIP.

Right now, MoSKito Control is at the stage of intensive development, with the current version 0.2.
However, MoSKito Control will be located as additional module, out of the [MoSKito primary code base](http://svn.anotheria.net/opensource/moskito/trunk/) on [anotheria GitHub account](https://github.com/anotheria). 

The preview image below shows the current look of the app.

![image](https://github.com/anotheria/moskito-control/blob/590f8d77505f43b5d47254e2813c037e894600a1/docs/images/moskito_control_v_0_2.png?raw=true)