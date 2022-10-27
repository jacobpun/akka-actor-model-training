package akka;

import akka.actor.typed.ActorSystem;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        ActorSystem<GuardianBehavior.GuardianMessage> actorSystem = ActorSystem.create(GuardianBehavior.create(), "math-guardian-actor");
        actorSystem.tell(new GuardianBehavior.GuardianAddRequest(100, 200));

        actorSystem.tell(new GuardianBehavior.GuardianSubtractRequest(50, 200));
        Thread.sleep(1000);
        actorSystem.terminate();
    }
}
