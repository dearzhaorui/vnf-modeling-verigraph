package ruiNFs;

import java.util.List;
import java.util.ArrayList;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.DatatypeExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Sort;
import it.polito.verigraph.mcnet.components.NetContext;
import it.polito.verigraph.mcnet.components.Network;
import it.polito.verigraph.mcnet.components.NetworkObject;
import it.polito.verigraph.mcnet.components.Tuple;

public class Rule_UserAgentClient extends NetworkObject {
	List<BoolExpr> constraints;
	Context ctx;
	DatatypeExpr n_UserAgentClient;
	Network net;
	NetContext nctx;
	FuncDecl isInternal;

	public Rule_UserAgentClient(Context ctx, Object[]... args) {
		super(ctx, args);
	}

	@Override
	protected void init(Context ctx, Object[]... args) {
		this.ctx = ctx;
		isEndHost = false;
		constraints = new ArrayList<BoolExpr>();
		z3Node = ((NetworkObject) args[0][0]).getZ3Node();
		n_UserAgentClient = z3Node;
		net = (Network) args[0][1];
		nctx = (NetContext) args[0][2];
		net.saneSend(this);
		isInternal = ctx.mkFuncDecl(n_UserAgentClient + "_isInternal", nctx.address, ctx.mkBoolSort());
	}

	@Override
	public DatatypeExpr getZ3Node() {
		return n_UserAgentClient;
	}

	@Override
	protected void addConstraints(Solver solver) {
		BoolExpr[] constr = new BoolExpr[constraints.size()];
		solver.add(constraints.toArray(constr));
	}

	public void setInternalAddress(ArrayList<DatatypeExpr> internalAddress) {
		List<BoolExpr> constr = new ArrayList<BoolExpr>();
		Expr in_0 = ctx.mkConst(n_UserAgentClient + "_internal_node", nctx.address);
		for (DatatypeExpr n : internalAddress)
			constr.add(ctx.mkEq(in_0, n));
		BoolExpr[] constrs = new BoolExpr[constr.size()];
		constraints.add(ctx.mkForall(new Expr[] { in_0 },
				ctx.mkEq(isInternal.apply(in_0), ctx.mkOr(constr.toArray(constrs))), 1, null, null, null, null));
	}

	public void installUserAgentClient(Expr ip_sipServer, Expr num_Callee) {
		Expr n_0 = ctx.mkConst("n_UserAgentClient_" + n_UserAgentClient + "_n_0", nctx.node);
		Expr n_1 = ctx.mkConst("n_UserAgentClient_" + n_UserAgentClient + "_n_1", nctx.node);
		Expr p_0 = ctx.mkConst("n_UserAgentClient_" + n_UserAgentClient + "_p_0", nctx.packet);
		Expr p_1 = ctx.mkConst("n_UserAgentClient_" + n_UserAgentClient + "_p_1", nctx.packet);
		constraints
				.add(ctx.mkForall(
						new Expr[] { p_0,
								n_0 },
						ctx.mkImplies(
								ctx.mkAnd((BoolExpr) nctx.send.apply(n_UserAgentClient, n_0, p_0),
									/*	ctx.mkEq(nctx.pf.get("proto").apply(p_1), ctx.mkInt(nctx.SIP_INVITE)),*/
										ctx.mkEq(nctx.pf.get("proto").apply(p_0), ctx.mkInt(nctx.SIP_OK))),
								ctx.mkExists(new Expr[] { p_1, n_1 }, ctx.mkAnd(
										(BoolExpr) nctx.recv.apply(n_1, n_UserAgentClient, p_1),
										ctx.mkEq(nctx.pf.get("proto").apply(p_1), ctx.mkInt(nctx.SIP_INVITE)),
										ctx.mkEq(nctx.pf.get("src").apply(p_0), nctx.pf.get("dest").apply(p_1)),
										ctx.mkEq(nctx.pf.get("dest").apply(p_0), nctx.pf.get("src").apply(p_1)),
										ctx.mkEq(nctx.src_port.apply(p_0), nctx.dest_port.apply(p_1)),
										ctx.mkEq(nctx.dest_port.apply(p_0), nctx.src_port.apply(p_1)),
										ctx.mkEq(nctx.pf.get("proto").apply(p_0), ctx.mkInt(nctx.SIP_OK)),
										ctx.mkEq(nctx.pf.get("transport_protocol").apply(p_0),
												nctx.pf.get("transport_protocol").apply(p_1)),
										ctx.mkEq(nctx.pf.get("origin").apply(p_0), nctx.pf.get("origin").apply(p_1)),
										ctx.mkEq(nctx.pf.get("orig_body").apply(p_0),
												nctx.pf.get("orig_body").apply(p_1)),
										ctx.mkEq(nctx.pf.get("body").apply(p_0), nctx.pf.get("body").apply(p_1)),
										ctx.mkEq(nctx.pf.get("seq").apply(p_0), nctx.pf.get("seq").apply(p_1)),
										ctx.mkEq(nctx.pf.get("emailFrom").apply(p_0),
												nctx.pf.get("emailFrom").apply(p_1)),
										ctx.mkEq(nctx.pf.get("url").apply(p_0), nctx.pf.get("url").apply(p_1)),
										ctx.mkEq(nctx.pf.get("options").apply(p_0), nctx.pf.get("options").apply(p_1)),
										ctx.mkEq(nctx.pf.get("oldSrc").apply(p_0), nctx.pf.get("oldSrc").apply(p_1)),
										ctx.mkEq(nctx.pf.get("oldDest").apply(p_0), nctx.pf.get("oldDest").apply(p_1)),
										ctx.mkEq(nctx.pf.get("inner_src").apply(p_0),
												nctx.pf.get("inner_src").apply(p_1)),
										ctx.mkEq(nctx.pf.get("inner_dest").apply(p_0),
												nctx.pf.get("inner_dest").apply(p_1)),
										ctx.mkEq(nctx.pf.get("encrypted").apply(p_0),
												nctx.pf.get("encrypted").apply(p_1))),
										1, null, null, null, null)),
						1, null, null, null, null));
		constraints.add(ctx
				.mkForall(
						new Expr[] { p_0,
								n_0 },
						ctx.mkImplies(
								ctx.mkAnd((BoolExpr) nctx.send.apply(n_UserAgentClient, n_0, p_0),
									/*	ctx.mkEq(nctx.pf.get("proto").apply(p_1), ctx.mkInt(nctx.SIP_OK)),*/
										ctx.mkEq(nctx.pf.get("proto").apply(p_0), ctx.mkInt(nctx.SIP_ENDING))),
								ctx.mkExists(new Expr[] { p_1, n_1 }, ctx.mkAnd(
										(BoolExpr) nctx.recv.apply(n_1, n_UserAgentClient, p_1),
										ctx.mkEq(nctx.pf.get("proto").apply(p_1), ctx.mkInt(nctx.SIP_OK)),
										ctx.mkEq(nctx.pf.get("src").apply(p_0), nctx.pf.get("dest").apply(p_1)),
										ctx.mkEq(nctx.pf.get("dest").apply(p_0), nctx.pf.get("src").apply(p_1)),
										ctx.mkEq(nctx.src_port.apply(p_0), nctx.dest_port.apply(p_1)),
										ctx.mkEq(nctx.dest_port.apply(p_0), nctx.src_port.apply(p_1)),
										ctx.mkEq(nctx.pf.get("proto").apply(p_0), ctx.mkInt(nctx.SIP_ENDING)),
										ctx.mkEq(nctx.pf.get("transport_protocol").apply(p_0),
												nctx.pf.get("transport_protocol").apply(p_1)),
										ctx.mkEq(nctx.pf.get("origin").apply(p_0), nctx.pf.get("origin").apply(p_1)),
										ctx.mkEq(nctx.pf.get("orig_body").apply(p_0),
												nctx.pf.get("orig_body").apply(p_1)),
										ctx.mkEq(nctx.pf.get("body").apply(p_0), nctx.pf.get("body").apply(p_1)),
										ctx.mkEq(nctx.pf.get("seq").apply(p_0), nctx.pf.get("seq").apply(p_1)),
										ctx.mkEq(nctx.pf.get("emailFrom").apply(p_0),
												nctx.pf.get("emailFrom").apply(p_1)),
										ctx.mkEq(nctx.pf.get("url").apply(p_0), nctx.pf.get("url").apply(p_1)),
										ctx.mkEq(nctx.pf.get("options").apply(p_0), nctx.pf.get("options").apply(p_1)),
										ctx.mkEq(nctx.pf.get("oldSrc").apply(p_0), nctx.pf.get("oldSrc").apply(p_1)),
										ctx.mkEq(nctx.pf.get("oldDest").apply(p_0), nctx.pf.get("oldDest").apply(p_1)),
										ctx.mkEq(nctx.pf.get("inner_src").apply(p_0),
												nctx.pf.get("inner_src").apply(p_1)),
										ctx.mkEq(nctx.pf.get("inner_dest").apply(p_0),
												nctx.pf.get("inner_dest").apply(p_1)),
										ctx.mkEq(nctx.pf.get("encrypted").apply(p_0),
												nctx.pf.get("encrypted").apply(p_1))),
										1, null, null, null, null)),
						1, null, null, null, null));
		constraints.add(ctx.mkForall(new Expr[] { p_0, n_0 },
				ctx.mkImplies((BoolExpr) nctx.send.apply(n_UserAgentClient, n_0, p_0),
						ctx.mkAnd(ctx.mkEq(nctx.pf.get("dest").apply(p_0), ip_sipServer),
								ctx.mkEq(nctx.pf.get("body").apply(p_0), num_Callee),
								ctx.mkEq(nctx.pf.get("proto").apply(p_0), ctx.mkInt(nctx.SIP_INVITE)))),
				1, null, null, null, null));
		
		constraints.add(ctx.mkForall(new Expr[] { p_0, n_0 },
				ctx.mkImplies((BoolExpr) nctx.send.apply(n_UserAgentClient, n_0, p_0),
						ctx.mkAnd(ctx.mkEq(nctx.pf.get("dest").apply(p_0), ip_sipServer),
								ctx.mkEq(nctx.pf.get("body").apply(p_0), num_Callee),
								ctx.mkEq(nctx.pf.get("proto").apply(p_0), ctx.mkInt(nctx.SIP_REGISTER)))),
				1, null, null, null, null));
		
	}
}