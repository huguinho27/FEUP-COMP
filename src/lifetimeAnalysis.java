import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class LifetimeAnalysis {
	private ArrayList<Variable> variables;
	private Register[] registers;

	public LifetimeAnalysis(int registers) {
		this.registers = new Register[registers];
		variables = new ArrayList<Variable>();
	}

	/**
	 * @return the registers
	 */
	public Register[] getRegisters() {
		return registers;
	}

	public void addVariable(Variable v) {
		variables.add(v);
	}

	public Variable getVariableByName(String name) {
		for (int i = 0; i < variables.size(); i++) {
			Variable v = variables.get(i);
			if (v.getName() == name)
				return v;
		}
		return null;
	}

	public int getVariableIndex(String name) {
		for (int i = 0; i < variables.size(); i++) {
			Variable v = variables.get(i);
			if (v.getName() == name)
				return i;
		}
		return -1;
	}

	public void parseNode(SimpleNode s) {
		// System.out.println(toString(prefix));
		if (s.children != null) {
			for (int i = 0; i < s.children.length; ++i) {
				SimpleNode n = (SimpleNode) s.children[i];
				if (n != null) {
					if (n.value.equals(":=")) {
						addVariable(n);
						parseNode(n);
					}

				}
			}
		}
	}

	public void addVariable(SimpleNode s) {
		int start = variables.size();

		SimpleNode n = (SimpleNode) s.children[0];
		variables.add(new Variable((String) n.value, start));
	}

	public void printLifetime(SimpleNode s) {
		for (int i = 0; i < variables.size(); i++) {
			Variable v = variables.get(i);

			v.setEnd(calculateEnd(s, v.getName()));
			System.out.println(v.getName() + " -> Start = " + v.getStart() + ", End = " + v.getEnd() + ", Range = "
					+ v.getLifeRange());
		}
	}

	public void printLifetimeGraph(SimpleNode s) {
		System.out.print("\n      ");
		for (int a = 0; a < variables.size(); a++) {
			System.out.print(variables.get(a).getName() + " ");
		}
		System.out.print("\n");
		for (int i = 0; i < variables.size(); i++) {
			Variable v = variables.get(i);

			v.setEnd(calculateEnd(s, v.getName()));
			System.out.print("\n" + v.getName() + " -> ");
			for (int j = 0; j < v.getStart(); j++) {
				System.out.print("   ");
			}
			for (int k = 0; k < v.getLifeRange(); k++) {
				System.out.print("-o-");
			}
		}
		System.out.print("\n");
	}

	public int calculateEnd(SimpleNode s, String varName) {
		int last = -1;
		if (s.children != null) {
			for (int i = 0; i < s.children.length; ++i) {
				SimpleNode n = (SimpleNode) s.children[i];

				if (parseNodes(n, varName)) {
					last = i;
				}
			}
		}
		return last;
	}

	public boolean parseNodes(SimpleNode s, String varName) {
		if (s.children != null) {
			for (int i = 0; i < s.children.length; ++i) {
				SimpleNode n = (SimpleNode) s.children[i];
				if (n != null) {
					// System.out.println(i + ": " + n.value + " " + regName);
					if (n.value.equals("*") || n.value.equals("+") || n.value.equals("-") || n.value.equals("/")) {
						if (parseExpr(n, varName))
							return true;
					}
				}
			}
		}
		return false;
	}

	public boolean parseExpr(SimpleNode s, String varName) {
		if (s.children != null) {
			for (int i = 0; i < s.children.length; ++i) {
				SimpleNode n = (SimpleNode) s.children[i];
				if (n != null) {
					if (n.value.equals(varName)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void leftEdgeAllocation() {

	}

	/*
	 * public void parseNewRegister(SimpleNode s) { SimpleNode n = (SimpleNode)
	 * s.children[0]; registers.addElement(new register((String) n.value)); }
	 * 
	 * public void printLifetime(SimpleNode s) { for (int i = 0; i <
	 * registers.size();i++) {
	 * System.out.println(lifetimePerRegister(s,registers.get(i).varName)); } }
	 * 
	 * public int lifetimePerRegister(SimpleNode s, String regName) { int last =
	 * -1; if (s.children != null) { for (int i = 0; i < s.children.length; ++i)
	 * { SimpleNode n = (SimpleNode) s.children[i];
	 * //System.out.println("node"+i); if(parseNodes(n,regName)){ last =i; }
	 * 
	 * }
	 * 
	 * } return last; }
	 * 
	 * public boolean parseNodes(SimpleNode s,String regName){
	 * 
	 * 
	 * if (s.children != null) { for (int i = 0; i < s.children.length; ++i) {
	 * SimpleNode n = (SimpleNode) s.children[i]; if (n != null) { //
	 * System.out.println(i + ": " + n.value + " " + regName); if
	 * (n.value.equals("*") || n.value.equals("+") || n.value.equals("-") ||
	 * n.value.equals("/")) { if(parseExpr(n,regName)) return true; }
	 * 
	 * } } }
	 * 
	 * 
	 * return false;
	 * 
	 * }
	 * 
	 * public boolean parseExpr(SimpleNode s,String regName){
	 * 
	 * if (s.children != null) { for (int i = 0; i < s.children.length; ++i) {
	 * SimpleNode n = (SimpleNode) s.children[i]; if (n != null) { //
	 * System.out.println(i + ": " + n.value + " " + regName); if
	 * (n.value.equals(regName)) { return true; }
	 * 
	 * } } }
	 * 
	 * 
	 * return false;
	 * 
	 * }
	 */

}