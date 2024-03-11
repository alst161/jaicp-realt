function initHandlers(){
    bind("preProcess", function($context) {
        if ($context.currentState.indexOf("/Fallback") !== -1) {
            $context.session.fallbackCounter =  $context.session.fallbackCounter + 1;
        }else if ($context.currentState == "/NoInput") {
            $context.session.noInputCounter =  $context.session.noInputCounter + 1;
        }else{
            if($context.session.fallbackCounter > 0 ){
                $context.session.fallbackCounter = 0;
            }
            if($context.session.noInputCounter > 0 ){
                $context.session.noInputCounter = 0;
            }
        }
    });
}
