package ai.tomorrow.movietime.overview

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
//class MoviePosterGridAdapter( val onClickListener: OnClickListener ) :
//    ListAdapter<MovieNetwork, MoviePosterGridAdapter.MoviePropertyViewHolder>(DiffCallback) {
//
//    /**
//     * Allows the RecyclerView to determine which items have changed when the [List] of [MovieNetwork]
//     * has been updated.
//     */
//    companion object DiffCallback : DiffUtil.ItemCallback<MovieNetwork>() {
//        override fun areItemsTheSame(oldItem: MovieNetwork, newItem: MovieNetwork): Boolean {
//            return oldItem === newItem
//        }
//
//        override fun areContentsTheSame(oldItem: MovieNetwork, newItem: MovieNetwork): Boolean {
//            return oldItem.id == newItem.id
//        }
//    }
//
//    /**
//     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
//     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
//     */
//    class MoviePropertyViewHolder(private var binding: GridViewItemBinding):
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(movieProperty: MovieNetwork) {
//            binding.movie = movieProperty
//            // This is important, because it forces the data binding to execute immediately,
//            // which allows the RecyclerView to make the correct view size measurements
//            binding.executePendingBindings()
//        }
//    }
//
//    /**
//     * Create new [RecyclerView] item views (invoked by the layout manager)
//     */
//    override fun onCreateViewHolder(parent: ViewGroup,
//                                    viewType: Int): MoviePropertyViewHolder {
//        return MoviePropertyViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
//    }
//
//    /**
//     * Replaces the contents of a view (invoked by the layout manager)
//     */
//    override fun onBindViewHolder(holder: MoviePropertyViewHolder, position: Int) {
//        val movieProperty = getItem(position)
//        holder.itemView.setOnClickListener {
//            onClickListener.onClick(movieProperty)
//        }
//        holder.bind(movieProperty)
//    }
//
//    /**
//     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [MovieNetwork]
//     * associated with the current item to the [onClick] function.
//     * @param clickListener lambda that will be called with the current [MovieNetwork]
//     */
//    class OnClickListener(val clickListener: (movieProperty: MovieNetwork) -> Unit) {
//        fun onClick(movieProperty:MovieNetwork) = clickListener(movieProperty)
//    }
//}