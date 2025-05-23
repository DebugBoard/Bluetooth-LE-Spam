package de.simon.dankelmann.bluetoothlespam.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import de.simon.dankelmann.bluetoothlespam.Enums.AdvertisementState
import de.simon.dankelmann.bluetoothlespam.Models.AdvertisementSet
import de.simon.dankelmann.bluetoothlespam.Models.AdvertisementSetList
import de.simon.dankelmann.bluetoothlespam.R
import de.simon.dankelmann.bluetoothlespam.databinding.ListItemAdvertisementListBinding
import de.simon.dankelmann.bluetoothlespam.databinding.ListItemAdvertisementSetBinding


class AdvertisementSetCollectionExpandableListViewAdapter internal constructor(
    private val context: Context,
    val advertisementSetLists: List<AdvertisementSetList>,
    val dataList: HashMap<AdvertisementSetList, List<AdvertisementSet>>
) : BaseExpandableListAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var groupBinding: ListItemAdvertisementListBinding
    private lateinit var itemBinding: ListItemAdvertisementSetBinding

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.dataList[this.advertisementSetLists[listPosition]]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        view: View?,
        parent: ViewGroup
    ): View {
        var convertView = view
        val holder: ItemViewHolder
        if (convertView == null) {
            itemBinding = ListItemAdvertisementSetBinding.inflate(inflater)
            convertView = itemBinding.root
            holder = ItemViewHolder()
            holder.label = itemBinding.listItemAdvertisementSetTextView
            holder.checkbox = itemBinding.checkboxAdvertisementSet
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemViewHolder
        }

        val advertisementSet = getChild(listPosition, expandedListPosition) as AdvertisementSet

        var textColor = when (advertisementSet.currentlyAdvertising) {
            true -> context.resources.getColor(R.color.blue_normal, context.theme)
            false -> context.resources.getColor(R.color.text_color, context.theme)
        }

        if (advertisementSet.currentlyAdvertising) {
            if (advertisementSet.advertisementState == AdvertisementState.ADVERTISEMENT_STATE_SUCCEEDED) {
                textColor = context.resources.getColor(R.color.log_success, context.theme)
            } else if (advertisementSet.advertisementState == AdvertisementState.ADVERTISEMENT_STATE_FAILED) {
                textColor = context.resources.getColor(R.color.log_error, context.theme)
            }
        } else {
            textColor = context.resources.getColor(R.color.text_color, context.theme)
        }

        holder.label?.text = advertisementSet.title
        holder.label?.setTextColor(textColor)
        
        // Set checkbox state from the model
        holder.checkbox?.isChecked = advertisementSet.isChecked
        
        // Add listener to update the model when checkbox state changes
        holder.checkbox?.setOnClickListener { view ->
            val isChecked = (view as android.widget.CheckBox).isChecked
            advertisementSet.isChecked = isChecked
            // Prevent the click from propagating to the parent view
            view.isPressed = false
        }
        
        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[this.advertisementSetLists[listPosition]]!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.advertisementSetLists[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.advertisementSetLists.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        view: View?,
        parent: ViewGroup
    ): View {
        var convertView = view
        val holder: GroupViewHolder

        if (convertView == null) {
            groupBinding = ListItemAdvertisementListBinding.inflate(inflater)
            convertView = groupBinding.root
            holder = GroupViewHolder()
            holder.label = groupBinding.listItemAdvertisementSetList
            holder.checkbox = groupBinding.checkboxAdvertisementList
            convertView.tag = holder
        } else {
            holder = convertView.tag as GroupViewHolder
        }

        val advertisementSetList = getGroup(listPosition) as AdvertisementSetList

        val textColor = when (advertisementSetList.currentlyAdvertising) {
            true -> context.resources.getColor(R.color.blue_normal, context.theme)
            false -> context.resources.getColor(R.color.text_color, context.theme)
        }

        holder.label?.text = advertisementSetList.title
        holder.label?.setTextColor(textColor)
        
        // Set initial checkbox state based on children's state
        val allChildrenChecked = dataList[advertisementSetList]?.all { it.isChecked } ?: false
        holder.checkbox?.isChecked = allChildrenChecked
        
        // Handle checkbox click to update all children and prevent interference with group expansion
        holder.checkbox?.setOnClickListener { view ->
            val isChecked = (view as android.widget.CheckBox).isChecked
            
            // Update all children in this group
            dataList[advertisementSetList]?.forEach { advertisementSet ->
                advertisementSet.isChecked = isChecked
            }
            
            // Notify data set changed to refresh all child views
            notifyDataSetChanged()
            
            // Prevent the click from propagating to the parent view
            view.isPressed = false
        }

        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

    inner class ItemViewHolder {
        internal var label: TextView? = null
        internal var checkbox: android.widget.CheckBox? = null
    }

    inner class GroupViewHolder {
        internal var label: TextView? = null
        internal var checkbox: android.widget.CheckBox? = null
    }
}
