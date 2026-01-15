package com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pedidos.ChangePedidoStatus;

import com.github.guilhermemonte21.Ecommerce.Application.Exceptions.PedidoNotFoundException;
import com.github.guilhermemonte21.Ecommerce.Application.Gateway.PedidoGateway;
import com.github.guilhermemonte21.Ecommerce.Application.UseCase.Pagamento.Pagamento;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.PedidoDoVendedor;
import com.github.guilhermemonte21.Ecommerce.Domain.Entity.Pedidos;
import com.github.guilhermemonte21.Ecommerce.Domain.Enum.StatusPedido;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChangePedidoStatus implements IChangePedidoStatus{
    private final PedidoGateway gateway;


    public ChangePedidoStatus(PedidoGateway gateway) {
        this.gateway = gateway;

    }

    @Override
    public void ChangePedidosStatus(UUID IdPedido) {
        Pedidos pedidos = gateway.getById(IdPedido).orElseThrow(() -> new PedidoNotFoundException(IdPedido));

        boolean pagos = pedidos.getItens().stream().allMatch(p -> p.getStatus() == "Pago");
        boolean entregues = pedidos.getItens().stream().allMatch(p -> p.getStatus() == "Entregues");
        boolean enviados = pedidos.getItens().stream().allMatch(p -> p.getStatus() == "Enviado");
        if(pagos){
            pedidos.setStatus(StatusPedido.PAGO);
        }
        else if (entregues){
            pedidos.setStatus(StatusPedido.ENTREGUE);
        }
        else if (enviados){
            pedidos.setStatus(StatusPedido.ENVIADO);
        }
        else {
            pedidos.setStatus(StatusPedido.CRIADO);
        }

        gateway.save(pedidos);
    }
}
