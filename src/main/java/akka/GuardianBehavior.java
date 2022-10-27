package akka;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;


public class GuardianBehavior extends AbstractBehavior<GuardianBehavior.GuardianMessage> {

    public interface GuardianMessage {}

    public record GuardianAddRequest(long first, long second) implements GuardianMessage { }
    public record GuardianSubtractRequest(long first, long second) implements GuardianMessage { }

    private GuardianBehavior(ActorContext<GuardianMessage> context) {
        super(context);
    }

    public static Behavior<GuardianMessage> create() {
        return Behaviors.setup(GuardianBehavior::new);
    }

    @Override
    public Receive<GuardianMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(GuardianAddRequest.class, this::processAddRequest)
                .onMessage(GuardianSubtractRequest.class, this::processSubtractRequest)
                .onMessage(MathBehavior.MathResponse.class, this::processResponse)
                .build();
    }

    private Behavior<GuardianMessage> processSubtractRequest(GuardianSubtractRequest request) {
        MathBehavior.SubtractRequest subtractRequest = new MathBehavior.SubtractRequest(request.first(), request.second(), getContext().getSelf());
        // An actor can create other actors
        getContext().spawn(MathBehavior.create(), "math-behavior-subtract-" + Math.random()).tell(subtractRequest);

        return Behaviors.same();

//        return newReceiveBuilder()
//                .onMessage(MathBehavior.MathResponse.class, this::processResponse)
//                .build();
    }

    private Behavior<GuardianMessage> processAddRequest(GuardianAddRequest request) {
        // An actor can create other actors
        MathBehavior.AddRequest addRequest = new MathBehavior.AddRequest(request.first(), request.second(), getContext().getSelf());
        getContext().spawn(MathBehavior.create(), "math-behavior-add").tell(addRequest);

        return Behaviors.same();

//        return newReceiveBuilder()
//                .onMessage(MathBehavior.MathResponse.class, this::processResponse)
//                .build();
    }

    private Behavior<GuardianMessage> processResponse(MathBehavior.MathResponse response) {
        System.out.println("Received response: " + response.result() + ". Original Request: " + response.originalRequest());
        // getContext().getLog().info("Received response: " + response);

        return Behaviors.same();
    }
}
