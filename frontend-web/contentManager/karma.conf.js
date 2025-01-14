module.exports = function (config) {
    config.set({
      frameworks: ['jasmine', '@angular-devkit/build-angular'],
      plugins: [
        require('karma-jasmine'),
        require('karma-chrome-launcher'),
        require('karma-jasmine-html-reporter'),
        require('karma-coverage'),
        require('@angular-devkit/build-angular/plugins/karma')
      ],
      client: {
        clearContext: false, // Laat Jasmine-specs zien in de browser
      },
      coverageReporter: {
        dir: require('path').join(__dirname, './coverage/contentManager'),
        subdir: '.',
        reporters: [{ type: 'html' }, { type: 'text-summary' }],
      },
      reporters: ['progress', 'kjhtml'],
      port: 9876,
      colors: true,
      logLevel: config.LOG_INFO,
      autoWatch: true,
      browsers: ['Chrome'],
      singleRun: false,
      restartOnFileChange: true,
      browserDisconnectTimeout: 20000,
      browserNoActivityTimeout: 60000,
    });
  };
  