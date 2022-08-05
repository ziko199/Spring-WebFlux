package basic;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

public class FluxTest {

    @Test
    void firstFlux() {
        Flux.just("A", "B", "C")
                .log()
                .subscribe();
    }

    @Test
    void fluxList() {
        Flux.just(Arrays.asList("A", "B", "C"))
                .log()
                .subscribe();
    }

    @Test
    void fluxFromIterable() {
        Flux.fromIterable(Arrays.asList("A", "B", "C"))
                .log()
                .subscribe();
    }

    @Test
    void fluxFromRange() {
        Flux.range(10, 5)
                .log()
                .subscribe();
    }

    @Test
    void fluxFromInterval() throws Exception {
        Flux.interval(Duration.ofSeconds(1))
                .log()
                .subscribe();
        Thread.sleep(5000);
    }

    @Test
    void fluxFromIntervalWithTake() throws Exception {
        Flux.interval(Duration.ofSeconds(1))
                .log()
                .take(2)
                .subscribe();
        Thread.sleep(5000);
    }

    @Test
    void fluxWith_2_Request() {
        Flux.range(1, 5)
                .log()
                .subscribe(null,
                        null,
                        null,
                        s -> s.request(2));
    }

    @Test
    void fluxWith_9_Requests() {
        Flux.range(1, 5)
                .log()
                .subscribe(null,
                        null,
                        null,
                        s -> s.request(9));
    }

    @Test
    void fluxRequestsUsingBaseSubscriber() {
        Flux.range(1, 5)
                .log()
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        subscription.request(3L);
                    }
                });
    }

    @Test
    void fluxCustomSubscriber() {
        Flux.range(1, 10)
                .log()
                .subscribe(new BaseSubscriber<Integer>() {
                    int elementsTOProcess = 3;
                    int counter = 0;

                    public void hookOnSubscribe(Subscription subscription) {
                        System.out.println("Subscribed!");
                        request(elementsTOProcess);
                    }

                    public void hookOnNext(Integer value) {
                        counter++;
                        if (counter == elementsTOProcess) {
                            counter = 0;

                            Random random = new Random();
                            elementsTOProcess = random.ints(1,4)
                                    .findFirst().getAsInt();

                            request(elementsTOProcess);
                        }
                    }
                });
    }

    @Test
    void fluxLimitRate() {
        Flux.range(1, 12)
                .log()
                .limitRate(3)
                .subscribe();
    }
}
