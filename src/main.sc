require: questions.sc
require: scripts/handlers.js
require: scripts/functions.js

init: initHandlers()

theme: /
    state: Start
        q!: $regex</start>
        script:
            initSession();
        go!: /Start/AdActualCheck
        
        state: AdActualCheck
            audio: https://storage.googleapis.com/realt-audio2/halo.wav
                
            state: AdIsActual
                intent: /AdIsActual
                intent: /Agreement
                script:
                    $session.success = true;
                #a: Czy Państwo nie będą nic przeciwko temu żebym umieściła Państwa ofertę na naszym portalu internetowym? To dla Państwa całkowicie bezpłatne!
                audio: https://storage.googleapis.com/realt-audio2/can_copy_ad.wav
                
                state: CanCopyAgree
                    intent: /Agreement
                    intent: /OfferIsInteresting
                    #a: Dobrze, muszę zadać Państwu kilka pytań
                    audio: https://storage.googleapis.com/realt-audio2/need_questions.wav
                    go!: /Questions/Family
                    
                state: CanCopyRejection
                    intent: /Rejection
                    intent: /OfferIsNoInteresting
                    #a: Ale dla Państwa jest to całkowicie bezpłatne! Publikujemy Państwa ogłoszenie na naszej stronie internetowej i w ciągu zaledwie kilku godzin nasz przedstawiciel umówi Państwa na spotkanie z naszym klientem. Nie muszą Państwo nic robić. Zadam tylko kilka pytań na temat mieszkania. Możemy tak zrobić?
                    audio: https://storage.googleapis.com/realt-audio2/response_to_rejection.wav
                
                    state: CanCopyAgree2
                        intent: /Agreement
                        intent: /OfferIsInteresting
                        #a: Dobrze, muszę zadać Państwu kilka pytań
                        audio: https://storage.googleapis.com/realt-audio2/need_questions.wav
                        go!: /Questions/Family
                        
                    state: CanCopyRejection2
                        intent: /Rejection
                        intent: /OfferIsNoInteresting
                        #a: Czy mogę zapytać dlaczego? Jest to dla Państwa bezpłatne i nie musi Państwu nic robić.
                        audio: https://storage.googleapis.com/realt-audio2/response_to_rejection2.wav
                
                        state: CanCopyAgree3 
                            intent: /Agreement
                            intent: /OfferIsInteresting
                            #a: Dobrze, muszę zadać Państwu kilka pytań
                            audio: https://storage.googleapis.com/realt-audio2/need_questions.wav
                            go!: /Questions/Family
                        
                        state: CanCopyRejection3
                            intent: /Rejection
                            intent: /OfferIsNoInteresting
                            #a: Jasne. W takim razie przepraszam za niedogodności i życzę miłego dnia.
                            audio: https://storage.googleapis.com/realt-audio2/not_actual.wav
                            
                state: FallbackOffer
                    event: noMatch
                    if: $session.fallbackCounter > 3
                        script:
                            $dialer.setCallResult("Не распознано");
                        go!: /Hangup
                    else:
                        random:
                            #a: Przepraszam, nie zrozumiałam. Proszę mi owiedzić, czy są Państwo zainteresowany naszą ofertą?
                            audio: https://storage.googleapis.com/realt-audio2/offer_dont_understand.wav
                    
            state: NotActual
                intent: /Rejection
                intent: /AdIsNotActual
                script:
                    $session.success = true;
                #a: Jasne. W takim razie przepraszam za niedogodności i życzę miłego dnia.
                audio: https://storage.googleapis.com/realt-audio2/not_actual.wav
                go!: /Hangup
            
            state: FallbackActual
                event: noMatch || toState = "/AdActualCheck", onlyThisState = true
                if: $session.fallbackCounter > 3
                    script:
                        $dialer.setCallResult("Не распознано");
                    go!: /Hangup
                else:
                    random:
                        #a: Przykro mi, ale nadal tego nie rozumiem. Czy ogłoszenie o mieszkaniu nadal jest do wynajęcia?
                        audio: https://storage.googleapis.com/realt-audio2/fallback_actual.wav
    
    state: Commission
        intent!: /Commission
        #a: Nasze usługi są bezpłatne dla właścicieli mieszkań. Czy ja mogę dodać Państwa ogłoszenie do naszej strony internetowej?
        audio: https://storage.googleapis.com/realt-audio2/commission.wav
        go: /Start/AdActualCheck/AdIsActual
        
    state: CompanyName
        intent!: /CompanyName
        #a: Nasza strona nazywa się Unreal-Estate. Współpracujemy tylko ze sprawdzonymi klientami. Nasze usługi są bezpłatne dla właścicieli mieszkań. Czy ja mogę dodać Państwa ogłoszenie do naszej strony internetowej?
        audio: https://storage.googleapis.com/realt-audio2/company_name.wav
        go: /Start/AdActualCheck/AdIsActual
        
    state: AutoAnswer
        intent!: /AutoAnswer
        q!: wiadomość
        go!: /Hangup

    state: Fallback
        event!: noMatch
        if: $session.fallbackCounter > 3
            script:
                $dialer.setCallResult("Не распознано");
            go!: /Hangup
        else:
            random:
                audio: https://storage.googleapis.com/realt-audio2/fallback1.wav
                audio: https://storage.googleapis.com/realt-audio2/fallback2.wav
            go: {{$context.session.contextHistory[0].contextPath}}
    
    state: NoInput || noContext = true
        event!: speechNotRecognized
        if: $session.noInputCounter > 3
            script:
                $dialer.setCallResult("Плохое качество связи");
            go!: /Hangup
        else:
            audio: https://storage.googleapis.com/realt-audio2/alo_{{$session.noInputCounter}}.wav
            
    state: Agent
        intent!: /IamAgent
        go!: /Hangup
                
    state: Hangup
        event!: hangup
        event!: botHangup
        script:
            onHangUp();
            $dialer.hangUp();
        