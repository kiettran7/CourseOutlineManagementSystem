import * as React from "react";
import { useTheme } from "@mui/material/styles";
import Box from "@mui/material/Box";
import OutlinedInput from "@mui/material/OutlinedInput";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import Chip from "@mui/material/Chip";

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
	PaperProps: {
		style: {
			maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
			width: 250,
		},
	},
};

function getStyles(name, personName, theme) {
	return {
		fontWeight:
			personName.indexOf(name) === -1
				? theme.typography.fontWeightRegular
				: theme.typography.fontWeightMedium,
	};
}

export default function MultipleSelectChip({
	subjects = [],
	items,
	handlePreSubjectChange,
}) {
	const names = subjects;
	const theme = useTheme();

	return (
		<div>
			<FormControl sx={{ m: 1, width: "100%" }}>
				<InputLabel id="demo-multiple-chip-label">
					Các môn học điều kiện
				</InputLabel>
				<Select
					labelId="demo-multiple-chip-label"
					id="demo-multiple-chip"
					multiple
					value={items}
					onChange={(e) => handlePreSubjectChange(e)}
					input={
						<OutlinedInput
							id="select-multiple-chip"
							label="Các môn học điều kiện"
						/>
					}
					renderValue={(selected) => (
						<Box
							sx={{ display: "flex", flexWrap: "wrap", gap: 0.5 }}
						>
							{selected.map((value) => {
								let a = subjects.filter((s) => s.id === value);
								if (a.length > 0)
									return (
										<Chip key={value} label={a[0].name} />
									);
							})}
						</Box>
					)}
					MenuProps={MenuProps}
				>
					{names.map((name, index) => {
						console.log("AAAA: ", name);
						return (
							<MenuItem
								key={index}
								value={name.id}
								style={getStyles(name.name, items, theme)}
							>
								{name.name}
							</MenuItem>
						);
					})}
				</Select>
			</FormControl>
		</div>
	);
}
