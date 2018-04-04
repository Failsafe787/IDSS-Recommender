/*
 * Released under GNU General Public License v3.0
 * @author Luca Banzato (Based on Dario Vogogna's code - github.com/akyrey)
 * @version 0.1.0
 */

import java.io.IOException;
import java.util.ArrayList;
import smile.*;
import utils.*;

public class Recommender {

	private static String PATH_TO_FILES = "sample_projects"; // Path for all xdls files
	private static String ES1 = "es1.xdsl";
	private static String ES2 = "es2.xdsl"; // NOTE: use the unrolled version for temporal models
	private static ArrayList<String> history;

	public static void main(String[] args) {
		boolean end = false;
		while (!end) { // Print menu until the user chooses to close the application
			int choice = mainMenu();
			if (choice == 0) {
				end = true;
			} else {
				String path = PATH_TO_FILES + "\\";
				switch (choice) {
				case 1:
					path += ES1;
					break;
				case 2:
					path += ES2;
					break;
				}
				try {
					processModel(path, choice);
				} catch (IOException e) {
					System.out.println("An error has occoured while opening the model");
					System.out.println(e.getMessage());
				}
			}
		}
		System.exit(0);
	}

	private static int mainMenu() {
		// Print the main menu and returns a value representing the model chosen by the
		// user
		System.out.println("IDSS - Project A.Y. 2016/2017");
		Utils.repeatChar('=', 30);
		System.out.println("1. Medical operation");
		System.out.println("2. Target-follower Robot");
		System.out.println("");
		System.out.println("0. Exit");
		System.out.println("");
		int choice = -1;
		boolean not_valid = true;
		while (not_valid) { // Exercise 1 or 2 is chosen
			choice = Input.readInt("Insert the model number you want to open: ");
			if (choice < 0 || choice > 2) {
				System.out.println("No models available for the specified entry!");
			} else {
				not_valid = false;
			}
		}
		System.out.println("");
		return choice;
	}

	private static int opMode(int lower_limit, int upper_limit) {
		// A mode choice is asked - lower_limit and upper_limit are used in order to
		// allow further developments which require adding a new mode

		System.out.println("Choose a decision mode");
		Utils.repeatChar('=', 31);
		System.out.println("1. Leave the user all the decisions");
		System.out.println("2. Leave the application all the decisions (best ones)");
		System.out.println("");
		System.out.println("0. Back");
		System.out.println("");
		int choice = -1;
		boolean not_valid = true;
		while (not_valid) { // Exercise choice
			choice = Input.readInt("Choose an option: ");
			if (choice < lower_limit || choice > upper_limit) {
				System.out.println("");
				System.out.println("Invalid option!");
				System.out.println("");
			} else {
				not_valid = false;
			}
		}
		System.out.println("");
		return choice;
	}

	private static int setOutcome(int lower_limit, int upper_limit) {
		// This method asks the user to set an observation in a range of choices between
		// [lower_limit,upper_limit]
		int choice = -1;
		boolean not_valid = true;
		System.out.println("");
		while (not_valid) { // An observed value is chosen here
			choice = Input.readInt("Specify a choice: ");
			if (choice < lower_limit || choice > upper_limit) {
				System.out.println("");
				System.out.println("Invalid option!");
				System.out.println("");
			} else {
				not_valid = false;
			}
		}
		System.out.println("");
		return choice;
	}

	private static void setParentsEvidence(NetworkNode node, Network network, int parent_node_id) {
		// Given a node and a parent id of a not observed node,
		// creates a parent network and asks the user some data
		NetworkNode parent_node = new NetworkNode(network, parent_node_id);
		System.out.println("The node \"" + parent_node.getName() + "\" must be observed to take a decision for \""
				+ node.getName() + "\"");
		System.out.println("");
		int index = 1;
		for (Tuple<String, Double> value : parent_node.getOutcomes()) {
			System.out.print(index + ") " + value + "\n");
			index++;
		}
		boolean valid = false;
		do {
			index = setOutcome(1, parent_node.getOutcomes().size());
			if (!parent_node.setEvidence(network, parent_node.getOutcomes().get(index - 1))) {
				System.out.println(
						"The choice made is not compatible with the previous decisions! Choose another value!");
			} else {
				valid = true;
				history.add(parent_node.getName() + " = " + (String) (parent_node.getOutcomes().get(index - 1).getX()));
			}
		} while (!valid);
	}

	private static Tuple<String, Double> autoDecision(Network network, ArrayList<Tuple<String, Double>> outcomes) {
		// Automatic decision function . In this case the best outcome is chosen.
		if (!outcomes.isEmpty()) {
			Tuple<String, Double> maxvalue = outcomes.get(0);
			for (Tuple<String, Double> value : outcomes) {
				if ((double) maxvalue.getY() < (double) value.getY()) {
					maxvalue = value;
				}
			}
			return maxvalue;
		} else {
			return null;
		}
	}

	private static void processModel(String path, int choice) throws IOException {
		// Prompt the user the choice between manual or auto mode
		int mode = opMode(0, 2);
		if (mode != 0) { // The user hasn't chosen to go back to the main menu
			// A Network is created and filled with datas imported from the GeNIe project
			// file
			history = new ArrayList<String>();
			Network network = new Network();
			network.readFile(path);
			network.updateBeliefs();
			// All the nodes are taken and, starting from the first one, a decision is
			// suggested for each of them
			int[] array_nodes = network.getAllNodes();
			for (int node_of_array : array_nodes) {
				if (network.getNodeType(node_of_array) == 17) { // ID 17 means a "decision" node
					NetworkNode node = new NetworkNode(network, node_of_array);

					// Check if all the decision node dependencies are satisfied
					for (int parent_node_id : node.getParents()) {
						if (!network.isEvidence(parent_node_id)) {
							setParentsEvidence(node, network, parent_node_id);
							network.updateBeliefs();
							Utils.repeatChar('=', 75);
							System.out.println("SUMMARY: " + Utils.printAL(history)); // Print a summary with the
																						// observed parents
							Utils.repeatChar('=', 75);
							System.out.println("");
						}
					}

					System.out.println("Possible values of the node \"" + node.getName() + "\"");
					// Outcomes are updated
					node.updateOutcomes(network);
					int entry_index = 1;
					// Outcomes are printed
					for (Tuple<String, Double> outcome : node.getOutcomes()) {
						System.out.print(entry_index + ") " + outcome + "\n");
						entry_index++;
					}
					if (mode == 1) { // A choice is required if the user is allowed to choose
						boolean valid = true;
						do {
							entry_index = setOutcome(1, node.getOutcomes().size());
							if (!node.setEvidence(network, node.getOutcomes().get(entry_index - 1))) {
								valid = false;
							}
						} while (!valid);
						history.add(node.getName() + " = " + (String) (node.getOutcomes().get(entry_index - 1).getX()));
					}

					else { // Otherwise the decision with the best utility is chosen and printed
						Tuple<String, Double> best = autoDecision(network, node.getOutcomes());
						if (best == null) {
							System.out.println("There's something wrong with the outcomes of a decision...");
							return;
						}
						node.setEvidence(network, best);
						history.add(node.getName() + " = " + (String) (best.getX()));
						Utils.repeatChar('=', 60);
						System.out.println("* Decision taken: " + best + " *\n");
					}
					network.updateBeliefs(); // Network is updated after a decision
					Utils.repeatChar('=', 75);
					System.out.println("SUMMARY: " + Utils.printAL(history)); // A summary with the decision taken is
																				// printed
					Utils.repeatChar('=', 75);
					System.out.println("");
				}

				else if (network.getNodeType(node_of_array) == 8 && choice == 1) // ID 8 indicates an "utility" node, it
																					// has a meaning
																					// printing it only for non temporal
																					// models
				{
					for (double a : network.getNodeValue(node_of_array)) {
						System.out.println("The utility of the choices taken is: " + a);
					}
				}
			}
			Utils.repeatChar('=', 100);
			System.out.println("Execution terminated. Returning to the main menu...");
			Utils.repeatChar('=', 100);
			System.out.println("");
		}
	}
}
