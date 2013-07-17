[MoSKito Control](https://confluence.opensource.anotheria.net/display/MSK/MoSKito+Control)
===============

## Definition
  
**MoSKito Control is a standalone web-based real-time performance monitoring tool.**

MoSKito Control is a universal monitoring station for your applications: it collects performance data from the monitored apps, processes the obtained info and presents it in a convenient, minimalistic and user-friendly way.

Presenting "a bird's eye view" of the monitored applications, it gives a clear picture of the app's health for both Developers and Managers, predicting any possible or upcoming problem.

## How It Works ##

MoSKito Control consists of two parts:

1. The **agent**, which is installed into the target (monitored) application, 
2. MoSKito Control **Web App** (Server).

**Agents** collect the performance info and send it to the web app via HTTP protocol. The **web app** receives the data, handles it and displays the processed info through its own **user interface**.

## Guides and Manuals ##

1. [MoSKito Control Quick Setup Guide](https://confluence.opensource.anotheria.net/display/MSK/MoSKito+Control+Quick+Setup+Guide) describes how to connect MoSKito Control with your application: install agents and configure Control web app itself.

2. [MoSKito Control Web App User Manual](https://confluence.opensource.anotheria.net/display/MSK/MoSKito+Control+Web+App+User+Manual) is a guide to the web app's user interface.

## History and Current State ##

Originally developed as Healthcheck Monitor for the PARSHIP GmbH, it has been contributed back to MoSKito codebase by PARSHIP.

Right now, MoSKito Control is at the stage of intensive development, with the current version 0.2.
However, MoSKito Control will be located as additional module, out of the [MoSKito primary code base](http://svn.anotheria.net/opensource/moskito/trunk/) on [anotheria GitHub account](https://github.com/anotheria). 

The preview image below shows the current look of the app.

![image](https://github.com/anotheria/moskito-control/blob/590f8d77505f43b5d47254e2813c037e894600a1/docs/images/moskito_control_v_0_2.png?raw=true)