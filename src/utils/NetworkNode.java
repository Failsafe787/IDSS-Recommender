/*
 * Released under GNU General Public License v3.0
 * @author Dario Vogogna - github.com/akyrey
 * @version 0.1.0
 */

package utils;

import java.util.ArrayList;
import smile.*;

public class NetworkNode {

	private int node_id;
	private String node_name;
	private ArrayList<Tuple<String, Double>> array_outcomes = new ArrayList<Tuple<String, Double>>();
	private int[] parents_id;

	// Constructor, requires a node ID and a Genie Network
	public NetworkNode(Network network, int node_id) {
		this.node_id = node_id;
		this.node_name = network.getNodeName(node_id);
		this.parents_id = network.getParents(node_id);
		// Creates an ArrayList of tuples with each tuple in the form Outcome(Name)-Value
		String[] outcomes = network.getOutcomeIds(node_id);
		double[] values = network.getNodeValue(node_id);
		for (int i = 0; i < network.getOutcomeCount(node_id); i++) {
			array_outcomes.add(new Tuple<String, Double>(outcomes[i], values[i]));
		}
	}

	// Network evidence is set in the network if the input evidence belongs to this node
	public boolean setEvidence(Network network, Tuple<String, Double> outcome) {
		if (array_outcomes.contains(outcome) && (double) outcome.getY() != 0) {
			network.setEvidence(this.node_id, (String) outcome.getX());
			return true;
		} else {
			return false;
		}
	}

	// Outcomes values aare updated after observing a value
	public void updateOutcomes(Network network) {
		array_outcomes.clear();
		String[] outcomes = network.getOutcomeIds(node_id);
		double[] values = network.getNodeValue(node_id);
		for (int i = 0; i < network.getOutcomeCount(node_id); i++) {
			array_outcomes.add(new Tuple<String, Double>(outcomes[i], values[i]));
		}
	}

	// Return node name
	public String getName() {
		return node_name;
	}

	// Returns an ArrayList of Tuples of the outcomes
	public ArrayList<Tuple<String, Double>> getOutcomes() {
		return array_outcomes;
	}

	// Returns an array of parents nodes IDs of this node
	public int[] getParents() {
		return parents_id;
	}
}
