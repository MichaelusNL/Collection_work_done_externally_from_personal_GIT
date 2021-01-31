// Java program to count smaller or equal
// elements in sorted array.

class GFG {

    // A binary search function. It returns
    // number of elements less than of equal
    // to given key
    static int binarySearchCount(int arr[], int n, int key)
    {
        int left = 0, right = n;

        int mid = 0;
        while (left < right) {
            mid = (right + left) >> 1;

            // Check if key is present in array
            if (arr[mid] == key) {

                // If duplicates are present it returns
                // the position of last element
                while (mid + 1 < n && arr[mid + 1] == key)
                    mid++;
                break;
            }

            // If key is smaller, ignore right half
            else if (arr[mid] > key)
                right = mid;

                // If key is greater, ignore left half
            else
                left = mid + 1;
        }

        // If key is not found in array then it will be
        // before mid
        while (mid > -1 && arr[mid] > key)
            mid--;

        // Return mid + 1 because of 0-based indexing
        // of array
        return mid + 1;
    }

    // Driver code
    public static void main(String[] args)
    {
        int arr[] = { 1, 2, 4, 5, 8, 10 };
        int key = 11;
        int n = arr.length;
        System.out.print(binarySearchCount(arr, n, key));
    }
}

