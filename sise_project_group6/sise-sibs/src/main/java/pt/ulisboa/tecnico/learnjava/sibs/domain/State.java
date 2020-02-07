//Carlos Morais nº3
//Guilherme Costa nº5
//grupo 6
package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.StateException;

public class State {
	public State prevState;

	public State() {

		super();
	}

	public void process(TransferOperation transfer, Sibs sibs) throws AccountException {

	}

	public void cancel(TransferOperation transfer, Sibs sibs) throws AccountException, StateException {

	}

	public int getCount() {

		return 0;
	}

	public void errorState(TransferOperation transfer, Sibs sibs) throws AccountException {

	}

	public void success(TransferOperation transfer, Sibs sibs) throws AccountException {

	}
}
