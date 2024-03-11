theme: /Questions
    state: Family
        q: *
        #a: Czy może być rodzina z dziećmi czy para?
        script:
            $session.agree = true;
        audio: https://storage.googleapis.com/realt-audio2/question_family.wav
            
        state: Foreinger
            q: *
            #a: Czy akceptuje Pani obcokrajowców?
            audio: https://storage.googleapis.com/realt-audio2/question_foreinger.wav
        
            state: Animal
                q: *
                #a: Czy zwierzęta są dozwolone?
                audio: https://storage.googleapis.com/realt-audio2/question_animals.wav

                state: WhenCanRent
                    q: *
                    #a: Od kiedy jest możliwe wprowadzenie?
                    audio: https://storage.googleapis.com/realt-audio2/question_rent.wav
        
                    state: Bye
                        q: *
                        #a: Dziękuję za odpowiedzi. Przekazałem ogłoszenie naszemu agentowi. Życzę miłego dnia.
                        audio: https://storage.googleapis.com/realt-audio2/question_bye.wav
                        go!: /Hangup

    