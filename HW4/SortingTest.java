import java.io.*;
import java.util.*;

public class SortingTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;
			int[] value;
			String nums = br.readLine();
			if (nums.charAt(0) == 'r')
			{
				isRandom = true;

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);
				int rminimum = Integer.parseInt(nums_arg[2]);
				int rmaximum = Integer.parseInt(nums_arg[3]);

				Random rand = new Random();

				value = new int[numsize];
				for (int i = 0; i < value.length; i++)
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{

				int numsize = Integer.parseInt(nums);

				value = new int[numsize];
				for (int i = 0; i < value.length; i++)
					value[i] = Integer.parseInt(br.readLine());
			}


			while (true)
			{
				int[] newvalue = (int[])value.clone();
                char algo = ' ';

				if (args.length == 4) {
                    return;
                }

				String command = args.length > 0 ? args[0] : br.readLine();

				if (args.length > 0) {
                    args = new String[4];
                }
				
				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'S':	// Search
						algo = DoSearch(newvalue);
						break;
					case 'X':
						return;
					default:
						throw new IOException("Wrong Input.");
				}
				if (isRandom)
				{

					System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
                    if (command.charAt(0) != 'S') {
                        for (int i = 0; i < newvalue.length; i++) {
                            System.out.println(newvalue[i]);
                        }
                    } else {
                        System.out.println(algo);
                    }
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("Wrong input Error : " + e.toString());
		}
	}

	//Every Sorting function is helped by chatGPT
	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoBubbleSort(int[] value)
	{
		int n = value.length;
		for (int i = 0; i < n-1; i++) {
			for (int j = 0; j < n-i-1; j++) {
				if (value[j] > value[j+1]) {
					int temp = value[j];
					value[j] = value[j+1];
					value[j+1] = temp;
				}
			}
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value)
	{
		for (int i = 1; i < value.length; i++) {
			int key = value[i];
			int j;
			for (j = i - 1; j >= 0 && value[j] > key; j--) {
				value[j + 1] = value[j];
			}
			value[j + 1] = key;

		}

		return value;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoHeapSort(int[] value)
	{

		int n = value.length;
		for (int i = n/2-1; i >= 0; i--) {
			heapify(value, n, i);
		}
		for (int i = n-1; i >= 0; i--) {
			int temp = value[0];
			value[0] = value[i];
			value[i] = temp;
			heapify(value, i, 0);
		}
		return (value);
	}

	private static void heapify(int[] value, int n, int i) {
		int largest = i;
		int left = 2*i+1;
		int right = 2*i+2;
		if (left < n && value[left] > value[largest]) {
			largest = left;
		}
		if (right < n && value[right] > value[largest]) {
			largest = right;
		}
		if (largest != i) {
			int temp = value[i];
			value[i] = value[largest];
			value[largest] = temp;
			heapify(value, n, largest);
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoMergeSort(int[] value)
	{
		return mergeSort(value);
	}
	private static int[] mergeSort(int[] value) {
	if (value.length <= 1) {
		return value;
	}
	int mid = value.length/2;
	int[] left = Arrays.copyOfRange(value, 0, mid);
	int[] right = Arrays.copyOfRange(value, mid, value.length);
	left = mergeSort(left);
	right = mergeSort(right);
	return merge(left, right);
}

	private static int[] merge(int[] left, int[] right) {
		int[] result = new int[left.length + right.length];
		int i = 0, j = 0, k = 0;
		while (i < left.length && j < right.length) {
			if (left[i] < right[j]) {
				result[k] = left[i];
				i++;
			} else {
				result[k] = right[j];
				j++;
			}
			k++;
		}
		while (i < left.length) {
			result[k] = left[i];
			i++;
			k++;
		}
		while (j < right.length) {
			result[k] = right[j];
			j++;
			k++;
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort(int[] value)
	{
		return QuickSort(value, 0,value.length-1);
	}
	private static int[] QuickSort(int[] value, int low, int high){
		if (low < high) {
			int pivotIndex = partition(value, low, high);
			QuickSort(value, low, pivotIndex-1);
			QuickSort(value, pivotIndex+1, high);
		}
		return value;
	}

	private static int partition(int[] value, int low, int high) {
		int pivot = value[high];
		int i = low-1;
		for (int j = low; j < high; j++) {
			if (value[j] < pivot) {
				i++;
				int temp = value[i];
				value[i] = value[j];
				value[j] = temp;
			}
		}
		int temp = value[i+1];
		value[i+1] = value[high];
		value[high] = temp;
		return i+1;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoRadixSort(int[] value)
	{
		// Find the maximum number to determine the number of digits
		int max = getMax(value);

		// Perform counting sort for every digit, including the sign digit
		for (int exp = 1; max / exp > 0; exp *= 10) {
			countingSort(value, exp);
		}
		return value;
	}

	private static int getMax(int[] value) {
		int max = Math.abs(value[0]);
		for (int i = 1; i < value.length; i++) {
			if (Math.abs(value[i]) > max) {
				max = Math.abs(value[i]);
			}
		}
		return max;
	}

	private static void countingSort(int[] value, int exp) {
		int n = value.length;
		int[] output = new int[n];
		int[] count = new int[19]; // 10 digits + 9 possible sign values (-9 to 9)

		// Initialize count value
		Arrays.fill(count, 0);

		// Store count of occurrences in count[]
		for (int i = 0; i < n; i++) {
			int digit = (value[i] / exp) % 10 + 9; // Adjust index for sign values
			count[digit]++;
		}

		// Change count[i] so that count[i] contains the actual position of this digit in output[]
		for (int i = 1; i < 19; i++) {
			count[i] += count[i - 1];
		}

		// Build the output array
		for (int i = n - 1; i >= 0; i--) {
			int digit = (value[i] / exp) % 10 + 9; // Adjust index for sign values
			output[count[digit] - 1] = value[i];
			count[digit]--;
		}

		// Copy the output array to array[]
		System.arraycopy(output, 0, value, 0, n);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
    private static char DoSearch(int[] value)
	{
		int sortedPairs = 0;
		int max = 0;
		int collisionCount = 0;
		Set<Integer> hashSet = new HashSet<>();

		for (int i=0; i<value.length; i++) {
			if (!hashSet.add(value[i])) {
				collisionCount++;
			}
			if(i!=value.length-1) {
				if (value[i] <= value[i + 1]) {
					sortedPairs++;
				}
			}
			if (Math.abs(value[i]) > max) {
				max = Math.abs(value[i]);
			}
		}

		if(max<=4){return 'R';}
		else if(1.0*sortedPairs/value.length>0.75){return 'H';}
		else if(1.0*collisionCount/value.length<0.25){return 'Q';}
		else return 'H';
	}




}
