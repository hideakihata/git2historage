package jp.ac.osaka_u.ist.sdl.scorpio;

import java.util.concurrent.atomic.AtomicInteger;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.DISSOLUTION;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.DefaultCFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IntraProceduralPDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.IPDGNodeFactory;

public class PDGBuildingThread<T extends CallableUnitInfo> implements Runnable {

	public PDGBuildingThread(final T[] methods, final AtomicInteger index,
			final IPDGNodeFactory pdgNodeFactory, final boolean data,
			final boolean control, final boolean execution,
			final boolean countObjectStateChange, final DISSOLUTION dissolve,
			final int dataDistance, final int controlDistance,
			final int executionDistance) {

		if (null == methods || null == index || null == pdgNodeFactory) {
			throw new IllegalArgumentException();
		}

		this.pdgNodeFactory = pdgNodeFactory;
		this.methods = methods;
		this.index = index;
		this.data = data;
		this.control = control;
		this.execution = execution;
		this.countObjectStateChange = countObjectStateChange;
		this.dissolve = dissolve;
		this.dataDistance = dataDistance;
		this.controlDistance = controlDistance;
		this.executionDistance = executionDistance;
	}

	@Override
	public void run() {

		while (true) {

			final int index = this.index.getAndIncrement();
			if (!(index < this.methods.length)) {
				break;
			}

			final IntraProceduralPDG pdg = new IntraProceduralPDG(
					this.methods[index], this.pdgNodeFactory,
					new DefaultCFGNodeFactory(), this.data, this.control,
					this.execution, this.countObjectStateChange, true,
					this.dissolve, this.dataDistance, this.controlDistance,
					this.executionDistance);
			PDGController.SINGLETON.put(this.methods[index], pdg);
		}
	}

	private final T[] methods;

	private final AtomicInteger index;

	private final IPDGNodeFactory pdgNodeFactory;

	private final boolean data;

	private final boolean control;

	private final boolean execution;

	private final boolean countObjectStateChange;

	private final DISSOLUTION dissolve;

	private final int dataDistance;

	private final int controlDistance;

	private final int executionDistance;
}
