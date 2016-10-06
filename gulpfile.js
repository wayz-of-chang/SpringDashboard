'use strict';

var gulp = require('gulp');
var concat = require('gulp-concat');
var changed = require('gulp-changed');
var stripDebug = require('gulp-strip-debug');
var rename = require('gulp-rename');
var uglify = require('gulp-uglify');
var runSequence = require('run-sequence');
var cleanCss = require('gulp-clean-css');

var DEST = 'src/main/resources/static/build/';

gulp.task('build-js', function() {
    return gulp.src([
            "src/main/resources/static/js/vendor/jquery-1.12.4.js",
            "src/main/resources/static/js/vendor/jquery.cookie.js",
            "src/main/resources/static/js/vendor/jquery-ui.js",
            "src/main/resources/static/js/vendor/jquery.iframe-transport.js",
            "src/main/resources/static/js/vendor/jquery.fileupload.js",
            "src/main/resources/static/js/vendor/sockjs-1.1.1.js",
            "src/main/resources/static/js/vendor/stomp.js",
            "src/main/resources/static/js/vendor/angular.js",
            "src/main/resources/static/js/vendor/angular-touch.js",
            "src/main/resources/static/js/vendor/bootstrap.js",
            "src/main/resources/static/js/vendor/d3.js",
            "src/main/resources/static/js/vendor/dagre.js",
            "src/main/resources/static/js/vendor/dagre-d3.js",
            "src/main/resources/static/js/statusIndicator.js",
            "src/main/resources/static/js/liquidFillGauge.js",
            "src/main/resources/static/js/barChart.js",
            "src/main/resources/static/js/lineChart.js",
            "src/main/resources/static/js/pieChart.js",
            "src/main/resources/static/js/application.js",
            "src/main/resources/static/js/angularDirectives.js",
            "src/main/resources/static/js/angularService.js",
            "src/main/resources/static/js/userController.js",
            "src/main/resources/static/js/dashboardController.js",
            "src/main/resources/static/js/monitorController.js",
            "src/main/resources/static/js/dataflowDiagram.js",
            "src/main/resources/static/js/datastructureDiagram.js",
            "src/main/resources/static/js/exampleCharts.js"
        ])
        .pipe(concat('urim.js'))
        .pipe(changed(DEST))
        .pipe(gulp.dest(DEST))
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(rename({ extname: '.min.js' }))
        .pipe(gulp.dest(DEST));
});

gulp.task('build-ng-js', function() {
    return gulp.src([
            "src/main/resources/static/js/vendor/jquery-1.12.4.js",
            "src/main/resources/static/js/vendor/jquery.cookie.js",
            "src/main/resources/static/js/vendor/jquery-ui.js",
            "src/main/resources/static/js/vendor/jquery.iframe-transport.js",
            "src/main/resources/static/js/vendor/jquery.fileupload.js",
            "src/main/resources/static/js/vendor/sockjs-1.1.1.js",
            "src/main/resources/static/js/vendor/stomp.js",
            "src/main/resources/static/js/vendor/angular.js",
            "src/main/resources/static/js/vendor/angular-animate.js",
            "src/main/resources/static/js/vendor/angular-aria.js",
            "src/main/resources/static/js/vendor/angular-messages.js",
            "src/main/resources/static/js/vendor/angular-material.js",
            "src/main/resources/static/js/vendor/ngDraggable.js",
            "src/main/resources/static/js/vendor/d3.js",
            "src/main/resources/static/js/vendor/dagre.js",
            "src/main/resources/static/js/vendor/dagre-d3.js",
            "src/main/resources/static/js/statusIndicator.js",
            "src/main/resources/static/js/liquidFillGauge.js",
            "src/main/resources/static/js/barChart.js",
            "src/main/resources/static/js/lineChart.js",
            "src/main/resources/static/js/pieChart.js",
            "src/main/resources/static/js/application-ng.js",
            "src/main/resources/static/js/angularDirectives.js",
            "src/main/resources/static/js/angularService.js",
            "src/main/resources/static/js/userController-ng.js",
            "src/main/resources/static/js/dashboardController-ng.js",
            "src/main/resources/static/js/monitorController-ng.js",
            "src/main/resources/static/js/dataflowDiagram.js",
            "src/main/resources/static/js/datastructureDiagram.js",
            "src/main/resources/static/js/exampleCharts.js"
        ])
        .pipe(concat('urim-ng.js'))
        .pipe(changed(DEST))
        .pipe(gulp.dest(DEST))
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(rename({ extname: '.min.js' }))
        .pipe(gulp.dest(DEST));
});

gulp.task('build-md-js', function() {
    return gulp.src([
            "src/main/resources/static/js/vendor/jquery-1.12.4.js",
            "src/main/resources/static/js/vendor/jquery.cookie.js",
            "src/main/resources/static/js/vendor/jquery-ui.js",
            "src/main/resources/static/js/vendor/jquery.iframe-transport.js",
            "src/main/resources/static/js/vendor/jquery.fileupload.js",
            "src/main/resources/static/js/vendor/sockjs-1.1.1.js",
            "src/main/resources/static/js/vendor/stomp.js",
            "src/main/resources/static/js/vendor/angular.js",
            "src/main/resources/static/js/vendor/angular-touch.js",
            "src/main/resources/static/js/vendor/materialize.js",
            "src/main/resources/static/js/vendor/d3.js",
            "src/main/resources/static/js/vendor/dagre.js",
            "src/main/resources/static/js/vendor/dagre-d3.js",
            "src/main/resources/static/js/statusIndicator.js",
            "src/main/resources/static/js/liquidFillGauge.js",
            "src/main/resources/static/js/barChart.js",
            "src/main/resources/static/js/lineChart.js",
            "src/main/resources/static/js/pieChart.js",
            "src/main/resources/static/js/application-md.js",
            "src/main/resources/static/js/angularDirectives.js",
            "src/main/resources/static/js/angularService.js",
            "src/main/resources/static/js/userController-md.js",
            "src/main/resources/static/js/dashboardController-md.js",
            "src/main/resources/static/js/monitorController-md.js",
            "src/main/resources/static/js/dataflowDiagram.js",
            "src/main/resources/static/js/datastructureDiagram.js",
            "src/main/resources/static/js/exampleCharts.js"
        ])
        .pipe(concat('urim-md.js'))
        .pipe(changed(DEST))
        .pipe(gulp.dest(DEST))
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(rename({ extname: '.min.js' }))
        .pipe(gulp.dest(DEST));
});

gulp.task('build-test-js', function() {
    return gulp.src([
            "src/main/resources/static/js/vendor/jquery-1.12.4.js",
            "src/main/resources/static/js/vendor/jquery.cookie.js",
            "src/main/resources/static/js/vendor/jquery-ui.js",
            "src/main/resources/static/js/vendor/jquery.iframe-transport.js",
            "src/main/resources/static/js/vendor/jquery.fileupload.js",
            "src/main/resources/static/js/vendor/sockjs-1.1.1.js",
            "src/main/resources/static/js/vendor/stomp.js",
            "src/main/resources/static/js/vendor/angular.js",
            "src/main/resources/static/js/vendor/angular-touch.js",
            "src/main/resources/static/js/vendor/bootstrap.js",
            "src/main/resources/static/js/vendor/d3.js",
            "src/main/resources/static/js/vendor/chai.js",
            "src/main/resources/static/js/vendor/mocha.js",
            "src/main/resources/static/js/test/application.js",
            "src/main/resources/static/js/vendor/angular-mocks.js",
            "src/main/resources/static/js/statusIndicator.js",
            "src/main/resources/static/js/liquidFillGauge.js",
            "src/main/resources/static/js/barChart.js",
            "src/main/resources/static/js/lineChart.js",
            "src/main/resources/static/js/pieChart.js",
            "src/main/resources/static/js/angularDirectives.js",
            "src/main/resources/static/js/angularService.js",
            "src/main/resources/static/js/userController.js",
            "src/main/resources/static/js/dashboardController.js",
            "src/main/resources/static/js/monitorController.js",
            "src/main/resources/static/js/test/userController.js",
            "src/main/resources/static/js/test/dashboardController.js",
            "src/main/resources/static/js/test/monitorController.js"
        ])
        .pipe(concat('urim-test.js'))
        .pipe(changed(DEST))
        .pipe(gulp.dest(DEST))
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(rename({ extname: '.min.js' }))
        .pipe(gulp.dest(DEST));
});

gulp.task('build-css', function() {
    return gulp.src([
            "src/main/resources/static/css/bootstrap.min.css", 
            "src/main/resources/static/css/bootstrap-theme.min.css", 
            "src/main/resources/static/css/font-awesome.min.css", 
            "src/main/resources/static/css/application.css"
        ])
        .pipe(concat('urim.css'))
        .pipe(changed(DEST))
        .pipe(gulp.dest(DEST))
        .pipe(cleanCss())
        .pipe(rename({ extname: '.min.css' }))
        .pipe(gulp.dest(DEST));
});

gulp.task('build-ng-css', function() {
    return gulp.src([
            "src/main/resources/static/css/angular-material.css",
            "src/main/resources/static/css/font-awesome.min.css",
            "src/main/resources/static/css/application-ng.css"
        ])
        .pipe(concat('urim-ng.css'))
        .pipe(changed(DEST))
        .pipe(gulp.dest(DEST))
        .pipe(cleanCss())
        .pipe(rename({ extname: '.min.css' }))
        .pipe(gulp.dest(DEST));
});

gulp.task('build-md-css', function() {
    return gulp.src([
            "src/main/resources/static/css/materialize.css",
            "src/main/resources/static/css/font-awesome.min.css",
            "src/main/resources/static/css/application-md.css"
        ])
        .pipe(concat('urim-md.css'))
        .pipe(changed(DEST))
        .pipe(gulp.dest(DEST))
        .pipe(cleanCss())
        .pipe(rename({ extname: '.min.css' }))
        .pipe(gulp.dest(DEST));
});

gulp.task('build-test-css', function() {
    return gulp.src([
            "src/main/resources/static/css/mocha.css"
        ])
        .pipe(concat('urim-test.css'))
        .pipe(changed(DEST))
        .pipe(gulp.dest(DEST))
        .pipe(cleanCss())
        .pipe(rename({ extname: '.min.css' }))
        .pipe(gulp.dest(DEST));
});

gulp.task('build', function() {
    runSequence(
        'build-js',
        'build-ng-js',
        'build-md-js',
        'build-test-js',
        'build-css',
        'build-ng-css',
        'build-md-css',
        'build-test-css'
    )
});

gulp.task('watch', ['build-js', 'build-ng-js', 'build-md-js', 'build-test-js', 'build-css', 'build-ng-css', 'build-md-css', 'build-test-css'], function
() {
    gulp.watch('src/main/resources/static/js/*.js', function() {
        gulp.run('build-js');
        gulp.run('build-ng-js');
        gulp.run('build-md-js');
        gulp.run('build-test-js');
    });

    gulp.watch('src/main/resources/static/js/*.js', function() {
        gulp.run('build-css');
        gulp.run('build-ng-css');
        gulp.run('build-md-css');
        gulp.run('build-test-css');
    });
});

