
import { Injectable } from '@angular/core';


@Injectable()
export class DataService {

  constructor() { }

  public scan_column_data = {
    version: "1.1.1-SNAPSHOT",
    configToggle: false,

    categories: [
      {
        active: true,
        all: true,
        health: "green",
        name: "All Categories",
        components: []
      },
      {
        active: false,
        all: false,
        health: "green",
        name: "web",
        components: []
      }
    ],

    widgets: [
      {
        name: "status",
        displayName: "Status",
        className: "statuses",
        icon: "fa fa-adjust",
        enabled: true
      },
      {
        name: "tv",
        displayName: "TV",
        className: "tv",
        icon: "fa fa-smile-o",
        enabled: false
      },
      {
        name: "charts",
        displayName: "Charts",
        className: "charts",
        icon: "fa fa-bar-chart-o",
        enabled: true
      },
      {
        name: "history",
        displayName: "History",
        className: "history",
        icon: "fa fa-bars",
        enabled: true
      }
    ],

    statistics: [
      {
        status: "purple",
        numberOfComponents: 0
      },
      {
        status: "red",
        numberOfComponents: 0
      },
      {
        status: "orange",
        numberOfComponents: 0
      },
      {
        status: "yellow",
        numberOfComponents: 0
      },
      {
        status: "green",
        numberOfComponents: 1
      }
    ]
  };


  public content_data = {
    configToggle: false,

    applications: [
      {
        name: "Burgershop",
        active: true,
        color: "green"
      }
    ],

    lastRefreshTimestamp: "2017-05-12T11:56:23,793",

    notificationsMuted: false,
    notificationsMutingTime: 60,
    notificationsRemainingMutingTime: 60,

    tvToggle: false,
    tvStatus: "green",

    statusToggle: true,
    componentHolders: [
      {
        health: "green",
        categoryName: "web",
        components: [
          {
            name: "burgershop",
            status: "green",
            messageCount: 0,
            messages: [],
            updateTimestamp: "2017-05-12T11:56:19,070",
          }
        ]
      }
    ],

    chartsToggle: true,
    chartBeans: [
      {
        name: "SessionCount",
        containerId: "SessionCount",
        limit: 100,
        lines: [
          {
            component: "burgershop",
            accumulator: "SessionCount Cur Absolute"
          }
        ]
      },
      {
        name: "OtherSessionCount",
        containerId: "OtherSessionCount",
        limit: 100,
        lines: [
          {
            component: "burgershop",
            accumulator: "SessionCount Cur Absolute"
          }
        ]
      },
      {
        name: "SessionCount1",
        containerId: "SessionCount1",
        limit: 100,
        lines: [
          {
            component: "burgershop",
            accumulator: "SessionCount Cur Absolute"
          }
        ]
      },
      {
        name: "OtherSessionCount1",
        containerId: "OtherSessionCount1",
        limit: 100,
        lines: [
          {
            component: "burgershop",
            accumulator: "SessionCount Cur Absolute"
          }
        ]
      },
      {
        name: "SessionCount2",
        containerId: "SessionCount2",
        limit: 100,
        lines: [
          {
            component: "burgershop",
            accumulator: "SessionCount Cur Absolute"
          }
        ]
      },
      {
        name: "OtherSessionCount2",
        containerId: "OtherSessionCount2",
        limit: 100,
        lines: [
          {
            component: "burgershop",
            accumulator: "SessionCount Cur Absolute"
          }
        ]
      }
    ],

    historyItems: [
      {
        time: "2017-05-12T11:56:01,023",
        componentName: "burgershop",
        oldStatus: "none",
        newStatus: "green"
      }
    ],

    configuration: {
      notificationsMutingTime: 60,
      historyItemsAmount: 100,
      applications: [
        {
          name: "Burgershop",
          components: [
            {
              name: "Burgershop",
              status: "green",
              category: "web",
              connectorType: "HTTP",
              location: "8080",
              messageCount: 0,
              messages: [],
              updateTimestamp: "2017-05-12T11:56:19,070"
            }
          ],

          charts: [
            {
              name: "SessionCount",
              containerId: "SessionCount",
              limit: 100,
              lines: [
                {
                  component: "burgershop",
                  accumulator: "SessionCount Cur Absolute"
                }
              ]
            },
            {
              name: "OtherSessionCount",
              containerId: "OtherSessionCount",
              limit: 100,
              lines: [
                {
                  component: "burgershop",
                  accumulator: "SessionCount Cur Absolute"
                }
              ]
            },
            {
              name: "SessionCount1",
              containerId: "SessionCount1",
              limit: 100,
              lines: [
                {
                  component: "burgershop",
                  accumulator: "SessionCount Cur Absolute"
                }
              ]
            },
            {
              name: "OtherSessionCount1",
              containerId: "OtherSessionCount1",
              limit: 100,
              lines: [
                {
                  component: "burgershop",
                  accumulator: "SessionCount Cur Absolute"
                }
              ]
            },
            {
              name: "SessionCount2",
              containerId: "SessionCount2",
              limit: 100,
              lines: [
                {
                  component: "burgershop",
                  accumulator: "SessionCount Cur Absolute"
                }
              ]
            },
            {
              name: "OtherSessionCount2",
              containerId: "OtherSessionCount2",
              limit: 100,
              lines: [
                {
                  component: "burgershop",
                  accumulator: "SessionCount Cur Absolute"
                }
              ]
            }
          ],

          connectors: [
            {
              type: "HTTP",
              className: "org.moskito.control.connectors.HttpConnector"
            },
            {
              type: "RMI",
              className: "org.moskito.control.connectors.RMIConnector"
            }
          ],

          statusUpdater: {
            checkPeriodInSeconds: 10,
            threadPoolSize: 10,
            timeoutInSeconds: 60,
            enabled: true
          },

          chartsUpdater: {
            checkPeriodInSeconds: 40,
            threadPoolSize: 5,
            timeoutInSeconds: 60,
            enabled: true
          }
        }
      ]
    }
  };
}
