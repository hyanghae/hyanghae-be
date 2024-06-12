package flaskspring.demo.utils.rabbit;

import flaskspring.demo.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Queue 로 메세지를 발핼한 때에는 RabbitTemplate 의 ConvertAndSend 메소드를 사용하고
 * Queue 에서 메세지를 구독할때는 @RabbitListener 을 사용
 *
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class RabbitMqService {

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;



    public void sendMember(Member member) {
        MemberDto memberDto = new MemberDto(member.getMemberId());
        log.info("member send: {}",memberDto);
        this.rabbitTemplate.convertAndSend(exchangeName,routingKey,memberDto);
    }

    /**
     * 1. Queue 로 메세지를 발행
     * 2. Producer 역할 -> Direct Exchange 전략
     **/
   /* public void sendMessage(MessageDto messageDto) {
        log.info("messagge send: {}",messageDto.toString());
        this.rabbitTemplate.convertAndSend(exchangeName,routingKey,messageDto);
    }*/
}