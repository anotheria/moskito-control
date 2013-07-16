moskito-control
===============

## Definition

**MoSKito Control is a standalone web-based real-time performance monitoring tool.**

MoSKito Control is a universal monitoring station for your applications: it collects performance data from the monitored apps, processes the obtained info and presents it in a convenient, minimalistic and user-friendly way.
Presenting "a bird's eye view" of the monitored applications, it gives a clear picture of the app's health for both Developers and Managers, predicting any possible or upcoming problem.

## How It Works

MoSKito Control consists of two parts:
1. The **agent** which is installed into the target (monitored) application, 
2. MoSKito Control **Web App**.

Agents collect the performance info and send it to the web app via HTTP protocol. The web app receives the data, handles it and displays the processed info through its own user interface.
