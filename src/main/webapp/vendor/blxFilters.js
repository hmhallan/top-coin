'use strict';

angular.module('blx.filters', [])

.filter('firstUpper', function($filter){
    return function(inputValue) {
        if (inputValue === undefined) {
            return "";
        }else{
            return inputValue.charAt(0).toUpperCase() + inputValue.substring(1);
        };
     };
})

.filter('telefone', function($filter, $locale) {
    return function (telefone) {
    	
    	if(angular.isUndefined(telefone)){
    		return '';
    	}else if(telefone.length >= 14){
            return telefone.substr(0, 3) + ' (' + telefone.substr(3, 2) + ') ' + telefone.substr(5, 5) + '-' + telefone.substr(10);
        }else if(telefone.length >= 13){
            return telefone.substr(0, 3) + ' (' + telefone.substr(3, 2) + ') ' + telefone.substr(5, 4) + '-' + telefone.substr(9);
        }else if(telefone.length >= 11){
            return '(' + telefone.substr(0, 2) + ') ' + telefone.substr(2, 5) + '-' + telefone.substr(7);
        }else if(telefone.length >= 10){
            return '(' + telefone.substr(0, 2) + ') ' + telefone.substr(2, 4) + '-' + telefone.substr(6);
        }else if(telefone.length >= 9){
            return telefone.substr(0, 5) + '-' + telefone.substr(5);
        }else if(telefone.length >= 8){
            return telefone.substr(0, 4) + '-' + telefone.substr(4);
        }else{
            return telefone;
        }
    };
})

.filter('yesno', function($filter, $locale) {
    return function (item) {
        return item > 0 ? 'Sim' : 'Não';
    };
})

.filter('histaction', function($filter) {
    return function (item) {
        switch(item){
            case 'update':
                return 'Atualização';
                break;
            case 'insert':
                return 'Criação';
                break;
            case 'delete':
                return 'Exclusão';
                break;
        };
    };
})

.filter('filesize', function($filter, $locale) {
    return function (item) {
        return $filter('number')((item/1024), 2) + ' KB';
    };
})

.filter('currency', function($filter, $locale) {
    return function (item, decimals) {
        return 'R$ '+$filter('number')(item, decimals);
    };
})

.filter('datetimept', function($filter) {
    return function (item) {
        if(angular.isUndefined(item) || item == null){
            return '-';
        }
        
        var timeStamp = new Date(item).toISOString();
        return $filter('date')(timeStamp, "dd/MM/yyyy H:mm");
    };
})

.filter('datept', function($filter) {
    return function (item) {
        if(angular.isUndefined(item) || item == null){
            return '-';
        }

        var timeStamp = new Date(item).toISOString();
        return $filter('date')(timeStamp, "dd/MM/yyyy");
    };
});