package nia.chapter2.echoserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Listing 2.1 EchoServerHandler
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private volatile int receiveCnt = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        String instr = in.toString(CharsetUtil.UTF_8);
        System.out.println(
                "Server received" + receiveCnt++ + ": " + instr);
        ctx.write(Unpooled.wrappedBuffer((instr + System.getProperty("line.separator")).getBytes()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
            throws Exception {
        System.out.println("Server channelReadComplete");
//        ctx.flush();
        ctx.writeAndFlush(Unpooled.wrappedBuffer(("finish" + System.getProperty("line.separator")).getBytes()))
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Server channelInactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
