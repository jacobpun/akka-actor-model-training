package akka;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class MathBehavior extends AbstractBehavior<MathBehavior.MathRequest> {


    public interface MathRequest extends GuardianBehavior.GuardianMessage {
        ActorRef<? extends GuardianBehavior.GuardianMessage> replyTo();
    }

    public record AddRequest(long first, long second, ActorRef<GuardianBehavior.GuardianMessage> replyTo) implements MathRequest {
    }

    public record SubtractRequest(long first, long second, ActorRef<GuardianBehavior.GuardianMessage> replyTo) implements MathRequest {
    }

    private MathBehavior(ActorContext<MathRequest> context) {
        super(context);
    }

    public record MathResponse(long result, MathRequest originalRequest) implements GuardianBehavior.GuardianMessage {
    }

    public static Behavior<MathRequest> create() {
        return Behaviors.setup(MathBehavior::new);
    }

    @Override
    public Receive<MathRequest> createReceive() {
        return newReceiveBuilder()
                .onMessage(AddRequest.class, this::add)
                .onMessage(SubtractRequest.class, this::subtract)
                .onAnyMessage(this::ignoreRequest)
                .build();
    }



    private Behavior<MathRequest> add(AddRequest request) {
        long result = request.first() + request.second();
        request.replyTo().tell(new MathResponse(result, request));
        return Behaviors.stopped();
    }

    private Behavior<MathRequest> subtract(SubtractRequest request) {
//         long result = request.first() - request.second();
//         request.replyTo().tell(new MathResponse(result, request));

        // An actor can send a message to "self"
        AddRequest addRequest = new AddRequest(request.first(), request.second() * -1, request.replyTo());
        this.getContext().getSelf().tell(addRequest);

        return Behaviors.same();
    }

    private Behavior<MathRequest> ignoreRequest(MathRequest mathRequest) {
        return Behaviors.same();
    }
}
