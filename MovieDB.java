import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MovieDB {
	private MovieGenreList movieList;
	private class MovieGenreList extends MyLinkedList<Genre>{
		@Override
		public void add(Genre genre){
			Node<Genre> point = head;

			while(point.getNext()!=null){
				if(point.getNext().getItem().equals(genre)){
					return;
				} else if(point.getNext().getItem().compareTo(genre)<0){
					point = point.getNext();
				}else{break;}
			}
			point.insertNext(genre);numItems++;
		}
	}
    public MovieDB() {
        // FIXME implement this
		movieList = new MovieGenreList();
    }

    public void insert(MovieDBItem item) {
        // FIXME implement this

		Genre genre=null;
		for(Genre genreIn : movieList){

			if(item.getGenre().equals(genreIn.getItem())){
				genre = genreIn;break;
			}
		}
		if(genre==null){
			genre = new Genre(item.getGenre());
			movieList.add(genre);
		}

		genre.add(item);
    }

    public void delete(MovieDBItem item) {
        // FIXME implement this

		for(Genre genre : movieList){
			MyLinkedListIterator<MovieDBItem> iterator = new MyLinkedListIterator<>(genre.movieDBItems);//genre.movieDBItems.iterator();
			while(iterator.hasNext()) {
				MovieDBItem movieDBItem = iterator.next();
				if (movieDBItem.equals(item)) {
					iterator.remove();break;
				}
			}
		}
		MyLinkedListIterator<Genre> genreIterator = new MyLinkedListIterator<>(movieList);//.iterator();
		while(genreIterator.hasNext()){
			Genre genre = genreIterator.next();
			if(genre.movieDBItems.size()==0){
				genreIterator.remove();
			}
		}

    }

    public MyLinkedList<MovieDBItem> search(String term) {
        // FIXME implement this

    	// FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
    	// This code is supplied for avoiding compilation error.   
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		for(Genre genre : movieList){
			for (MovieDBItem item1: genre.movieDBItems) {
				if(item1.contains(term)){
					results.add(item1);
				}
			}
		}

        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
        // FIXME implement this

    	// FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
    	// This code is supplied for avoiding compilation error.   
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		for(Genre genre : movieList){
			for (MovieDBItem item1: genre.movieDBItems) {
				results.add(item1);
			}
		}
        
    	return results;
    }
}

class Genre extends Node<String> implements Comparable<Genre> {
	public MyLinkedList<MovieDBItem> movieDBItems;
	public Genre(String name) {
		super(name);
		movieDBItems = new MyLinkedList<>();
	}
	public void add(MovieDBItem item) {
		Node<MovieDBItem> point = movieDBItems.head;

		while(point.getNext()!=null){
			if(point.getNext().getItem().equals(item)){
				return;
			} else if(point.getNext().getItem().compareTo(item)<0){
				point = point.getNext();
			}else{break;}
		}
		point.insertNext(item);movieDBItems.numItems++;

	}

	@Override
	public int compareTo(Genre o) {
		return this.getItem().compareTo(o.getItem());
	}
}

class MovieList implements ListInterface<String> {
	public MovieList() {
	}

	@Override
	public Iterator<String> iterator() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void add(String item) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public String first() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void removeAll() {
		throw new UnsupportedOperationException("not implemented yet");
	}
}

