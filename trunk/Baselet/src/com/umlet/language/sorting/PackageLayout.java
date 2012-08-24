package com.umlet.language.sorting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baselet.element.GridElement;
import com.umlet.element.Package;

import com.umlet.language.SortableElement;

public class PackageLayout extends Layout {

	@Override
	public void layout(List<SortableElement> elements) {
		List<SortableElement> packages = createPackageElements(extractPackages(elements).keySet());
		elements.addAll(packages);
	}

	private Map<String, List<SortableElement>> extractPackages(List<SortableElement> elements) {
		Map<String, List<SortableElement>> packages = new HashMap<String, List<SortableElement>>();
		for (SortableElement element: elements) {
			String packageElement = element.getParsedClass().getPackage();
			if (!packages.containsKey(packageElement)) {
				packages.put(packageElement, new ArrayList<SortableElement>());
			} 
			packages.get(packageElement).add(element);	
		}
		return packages;
	}
	
	private List<SortableElement> createPackageElements(Set<String> keySet) {
		List<SortableElement> packages = new ArrayList<SortableElement>();
		for (String name: keySet) {
			GridElement pack = new Package();
			pack.setPanelAttributes(name+"\nbg=orange");
			packages.add(new SortableElement(pack));
		}
		return packages;
	}
}
