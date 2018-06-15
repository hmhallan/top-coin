/**
 * https://github.com/gudh/ngecharts
 * License: MIT
 */

(function () {
    'use strict';

    var app = angular.module('ngecharts', [])
    app.directive('echarts', ['$window', function ($window) {
        return {
            restrict: 'EA',
            template: '<div></div>',
            scope: {
                options: '=options',
				theme: '=theme'
            },
            link: buildLinkFunc($window)
        };
    }]);

    function buildLinkFunc($window) {
        return function (scope, ele, attrs) {
            var chart, options;
            chart = echarts.init(ele[0], ( scope.theme || 'macarons' ) ); 

            createChart(scope.options);

            function createChart(options) {
                if (!options) return;

              //verifica se tem dados em alguma serie
                if (options.series && options.series.length && options.series[0].data && !options.series[0].data.length){
                    
                    options.noDataLoadingOption = {
                        text : 'sem dados para exibir',
                        effect : 'bar',
                        textStyle : {
                            fontSize : 20
                        }
                    };

                    options.loadingOption = {
                        text : 'Aguarde',
                        effect : 'whirling',
                        textStyle : {
                            fontSize : 20
                        }
                    };
                }
                else{
                    //se tiver, oculta o loading
                    chart.hideLoading();
                }
                
                chart.setOption(options, true); 

                angular.element($window).bind('resize', function(){
                    chart.resize();
                });

            }

            scope.$watch('options', function (newVal, oldVal) {
                if (angular.equals(newVal, oldVal)) return;
                createChart(newVal);
            })

            
        };
    }

})(); 

